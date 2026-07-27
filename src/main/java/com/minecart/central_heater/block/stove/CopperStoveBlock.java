package com.minecart.central_heater.block.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.stove.CopperStoveBlockEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CopperStoveBlock extends AbstractStoveBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final MapCodec<CopperStoveBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperStoveBlock::getWeatherState),
            propertiesCodec()
    ).apply(instance, CopperStoveBlock::new));

    private final WeatherState weatherState;

    public CopperStoveBlock(WeatherState weatherState, Properties properties) {
        super(properties.noOcclusion().lightLevel(state -> state.getValue(LIT) ? 13 : 0));
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.valueOf(false))
                .setValue(POWERED, Boolean.valueOf(false)));
    }

    @Override
    public MapCodec<? extends CopperStoveBlock> codec() {
        return CODEC;
    }

    public WeatherState getWeatherState() {
        return this.weatherState;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperStoveBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            return state.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
        }
        return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean hasSignal = level.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != hasSignal) {
                level.setBlock(pos, state.setValue(POWERED, hasSignal), 3);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    // Crucial: Prevent items spilling out when scraped or waxed!
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (newState.getBlock() instanceof CopperStoveBlock) {
                return;
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof CopperStoveBlockEntity entity && !entity.isRemoved()){
            Direction blockDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if(hitResult.getLocation().y - hitResult.getBlockPos().getY() >= 0.9375){
                ItemStack extract = entity.items.extractItem(getIndexFromHitResult(hitResult, blockDir), Item.ABSOLUTE_MAX_STACK_SIZE, false);
                for(RecipeHolder<SmeltingRecipe> recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING))
                    if(recipe.value().getResultItem(level.registryAccess()).is(extract.getItem()))
                        player.triggerRecipeCrafted(recipe, List.of(extract));
                player.setItemInHand(InteractionHand.MAIN_HAND, extract);
            } else {
                player.setItemInHand(InteractionHand.MAIN_HAND, entity.fuels.extractItem(false));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, AllBlockEntity.COPPER_STOVE.get(), CopperStoveBlockEntity::clientTick);
        } else {
            return createTickerHelper(blockEntityType, AllBlockEntity.COPPER_STOVE.get(), CopperStoveBlockEntity::serverTick);
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return !state.getValue(LIT).booleanValue();
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().campfire(), 2.0f);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double d0 = (double)pos.getX() + 0.5;
            double d1 = (double)pos.getY() + 0.75;
            double d2 = (double)pos.getZ() + 0.5;

            if (random.nextInt(state.getValue(POWERED) ? 5 : 10) == 0) {
                level.playLocalSound(d0, d1, d2, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
                        0.5F + random.nextFloat(), random.nextFloat() * 0.8F + 1F, false);
            }

            for (int i = 0; i < random.nextInt(1) + 1; i++) {
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, (random.nextFloat() / 2.0F), 5.0E-5, (random.nextFloat() / 2.0F));
            }

            for(int i=0;i<random.nextIntBetweenInclusive(4,6);i++){
                level.addAlwaysVisibleParticle(ParticleTypes.SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }
    }
}