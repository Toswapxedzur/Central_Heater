package com.minecart.central_heater.block.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.block_entity.stove.GoldenStoveBlockEntity;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GoldenStoveBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<NetherFireState> LIT_SOUL = EnumProperty.create("lit_soul", NetherFireState.class);

    public static final VoxelShape SHAPE = box(0,0,0,16,16,16);

    public GoldenStoveBlock(Properties properties) {
        super(properties.noOcclusion().lightLevel(lit -> {
            if(lit.getValue(LIT_SOUL).getState() == 2){ return 15; }
            else if(lit.getValue(LIT_SOUL).getState() == 1){ return 13; }
            else{ return 0; }
        }));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT_SOUL, NetherFireState.NONE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GoldenStoveBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT_SOUL);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, AllBlockEntity.red_nether_brick_stove.get(), GoldenStoveBlockEntity::clientTick);
        }else{
            return createTickerHelper(blockEntityType, AllBlockEntity.red_nether_brick_stove.get(), GoldenStoveBlockEntity::serverTick);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        if(level.getBlockEntity(pos) instanceof GoldenStoveBlockEntity entity && !entity.isRemoved()){
            Direction blockDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            ItemStack stack = player.getItemInHand(hand);

            // Use Item On logic
            if(!stack.isEmpty()) {
                if(stack.is(Items.FLINT_AND_STEEL)){
                    entity.kindle();
                }
                else if(hitResult.getDirection().equals(Direction.UP)){
                    player.setItemInHand(hand, entity.items.insertItem(getIndexFromHitResult(hitResult, blockDir), stack, false));
                }
                else {
                    player.setItemInHand(hand, entity.fuels.insertItem(stack, false));
                }
                return InteractionResult.SUCCESS;
            }
            // Use Without Item logic
            else {
                if(hitResult.getDirection().equals(Direction.UP)){
                    ItemStack extract = entity.items.extractItem(getIndexFromHitResult(hitResult, blockDir), Item.MAX_STACK_SIZE, false);

                    for(HauntingRecipe recipe : level.getRecipeManager().getAllRecipesFor(AllRecipe.HAUNTING.get()))
                        if(recipe.getResultItem(level.registryAccess()).is(extract.getItem()))
                            player.awardRecipes(Collections.singleton(recipe));

                    for(SmeltingRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING))
                        if(recipe.getResultItem(level.registryAccess()).is(extract.getItem()))
                            player.awardRecipes(Collections.singleton(recipe));

                    player.setItemInHand(InteractionHand.MAIN_HAND, extract);
                }
                else{
                    player.setItemInHand(InteractionHand.MAIN_HAND, entity.fuels.extractItem(false));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide)
            return;
        if(!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof GoldenStoveBlockEntity entity)
                entity.dropContent();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType pathComputationType) {
        return !state.getValue(LIT_SOUL).equals(NetherFireState.NONE);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockpos = hit.getBlockPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, blockpos) &&
                level.getBlockEntity(hit.getBlockPos()) instanceof GoldenStoveBlockEntity entity && !entity.isLit()) {
            entity.kindle();
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT_SOUL).equals(NetherFireState.BURN) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().inFire(), (float)2.0f);
        }
        if (state.getValue(LIT_SOUL).equals(NetherFireState.SOUL) && entity instanceof LivingEntity livingEntity) {
            livingEntity.hurt(level.damageSources().inFire(), (float)4.0f);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20));
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double)pos.getX() + 0.5;
        double d1 = (double)pos.getY() + 0.75;
        double d2 = (double)pos.getZ() + 0.5;

        if (!state.getValue(LIT_SOUL).equals(NetherFireState.NONE)) {
            if (random.nextInt(10) == 0) {
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

            for(int i=0;i<random.nextIntBetweenInclusive(1,3);i++){
                level.addParticle(ParticleTypes.LARGE_SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }

            for(int i=0;i<random.nextIntBetweenInclusive(1,2);i++){
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }

        if(state.getValue(LIT_SOUL).equals(NetherFireState.SOUL)){
            for(int i=0;i<random.nextIntBetweenInclusive(0,2);i++){
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        ((random.nextFloat()-0.5f) / 4.0F), 0.07, ((random.nextFloat()-0.5f) / 4.0F));
            }

            for(int i=0;i<random.nextIntBetweenInclusive(1,2);i++){
                level.addParticle(ParticleTypes.SOUL,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        ((random.nextFloat()-0.5f) / 4.0F), 0.07, ((random.nextFloat()-0.5f) / 4.0F));
            }
        }
        super.animateTick(state, level, pos, random);
    }

    private int getIndexFromHitResult(BlockHitResult result, Direction direction){
        Vec3 hitLoc = result.getLocation();
        int dir = direction.get2DDataValue();
        boolean b0 = hitLoc.x - Math.floor(hitLoc.x) > 0.5d;
        boolean b1 = hitLoc.z - Math.floor(hitLoc.z) > 0.5d;
        int index;
        if(b0 && b1)
            index = 2;
        else if (!b0 && b1)
            index = 3;
        else if (b0 && !b1)
            index = 1;
        else
            index = 0;
        return (index - dir + 4) % 4;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AbstractStoveBlockEntity stove) {
                stove.setPlacer(player.getUUID());
            }
        }
    }
}