package com.minecart.central_heater.block.cauldron;

import com.minecart.central_heater.block_entity.cauldron.ModCauldronBlockEntity;
import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;
import com.minecart.central_heater.heat.api.ThermalOverrideProvider;
import com.minecart.central_heater.misc.NewCauldronInteraction;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ModCauldronBlock extends BaseEntityBlock implements ThermalOverrideProvider {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
    public static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box((double)0.0F, (double)0.0F, (double)4.0F, (double)16.0F, (double)3.0F, (double)12.0F), new VoxelShape[]{box((double)4.0F, (double)0.0F, (double)0.0F, (double)12.0F, (double)3.0F, (double)16.0F), box((double)2.0F, (double)0.0F, (double)2.0F, (double)14.0F, (double)3.0F, (double)14.0F), box((double)2.0F, (double)4.0F, (double)2.0F, (double)14.0F, (double)16.0F, (double)14.0F)}), BooleanOp.ONLY_FIRST);
    public final int tier;
    private final Supplier<BlockEntityType<ModCauldronBlockEntity>> blockEntityType;

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ModCauldronBlockEntity(blockEntityType.get(), blockPos, blockState, tier);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, this.blockEntityType.get(), ModCauldronBlockEntity::clientTick);
        }else{
            return createTickerHelper(blockEntityType, this.blockEntityType.get(), ModCauldronBlockEntity::serverTick);
        }
    }

    public ModCauldronBlock(Properties properties, Supplier<BlockEntityType<ModCauldronBlockEntity>> blockEntityType, int tier) {
        super(properties.noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LEVEL)));
        this.blockEntityType = blockEntityType;
        this.tier = tier;
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LEVEL, Integer.valueOf(0)));
    }

    @Override
    public @Nullable ThermalMaterial getThermalMaterialOverride(BlockState state) {
        return switch (tier) {
            case 1 -> ThermalMaterial.MUD_BRICK;
            case 2 -> ThermalMaterial.BRICK;
            case 3 -> ThermalMaterial.METAL;
            case 4 -> ThermalMaterial.GOLD;
            default -> ThermalMaterial.BRICK;
        };
    }

    @Override
    public @Nullable ThermalMaterialBehavior getThermalBehaviorOverride(BlockState state) {
        return null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(p -> new ModCauldronBlock(p, this.blockEntityType, this.tier));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEVEL);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity && !entity.isRemoved()){
            if (FluidUtil.interactWithFluidHandler(player, hand, entity.getFluidTank())) {
                return ItemInteractionResult.SUCCESS;
            }
            NewCauldronInteraction interaction = NewCauldronInteraction.INTERACTIONS.get(stack.getItem());
            if (interaction != null) {
                ItemInteractionResult result = interaction.interact(level, entity, player, hand, stack);
                if (result.consumesAction()) {
                    return result;
                }
            }
            if (!stack.isEmpty()) {
                ItemStack remainder = entity.getContainer().insertItem(stack, true);
                if (remainder.getCount() < stack.getCount()) {
                    ItemStack actualRemainder = entity.getContainer().insertItem(stack, false);
                    player.setItemInHand(hand, actualRemainder);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity && !entity.isRemoved()) {
            ItemStack extract = entity.getContainer().extractItem(false);

            if (extract.isEmpty()) {
                return InteractionResult.PASS;
            }

            for (RecipeHolder<SmolderingRecipe> recipe : level.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get())) {
                if (recipe.value().getResults().stream().anyMatch(i -> ItemStack.isSameItem(i, extract))) {
                    player.triggerRecipeCrafted(recipe, List.of(extract));
                    break;
                }
            }

            player.setItemInHand(InteractionHand.MAIN_HAND, extract);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if(!oldState.is(this)) level.invalidateCapabilities(pos);
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!state.is(newState.getBlock())) {
            level.invalidateCapabilities(pos);
            if(level.isClientSide)
                return;
            if (level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity)
                entity.drop();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof net.minecraft.world.entity.item.ItemEntity itemEntity) {
            if (level.getBlockEntity(pos) instanceof ModCauldronBlockEntity cauldron && !cauldron.isRemoved()) {

                ItemStack stack = itemEntity.getItem();

                ItemStack remainder = cauldron.getContainer().insertItem(stack, false);

                if (remainder.isEmpty()) {
                    itemEntity.discard();
                } else if (remainder.getCount() < stack.getCount()) {
                    itemEntity.setItem(remainder);
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ModCauldronBlockEntity cauldron) {
                cauldron.setPlacer(player.getUUID());
            }
        }
    }
}
