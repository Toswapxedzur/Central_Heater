package com.minecart.central_heater.block.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import com.minecart.central_heater.block_entity.stove.CopperStoveBlockEntity;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CopperStoveBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape CENTER_HOLE = box(3, 3, 3, 13, 14, 13);
    public static final VoxelShape SHAPE_NORTH = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(0, 5, 3, 3, 12, 13)), BooleanOp.ONLY_FIRST);
    public static final VoxelShape SHAPE_EAST = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(3, 5, 0, 13, 12, 3)), BooleanOp.ONLY_FIRST);
    public static final VoxelShape SHAPE_SOUTH = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(13, 5, 3, 16, 12, 13)), BooleanOp.ONLY_FIRST);
    public static final VoxelShape SHAPE_WEST = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(3, 5, 13, 13, 12, 16)), BooleanOp.ONLY_FIRST);

    private final WeatherState weatherState;

    public CopperStoveBlock(WeatherState weatherState, Properties properties) {
        super(properties.noOcclusion().lightLevel(state -> state.getValue(LIT) ? 13 : 0));
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.FALSE)
                .setValue(POWERED, Boolean.FALSE));
    }

    public WeatherState getWeatherState() {
        return this.weatherState;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperStoveBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_SOUTH;
            case SOUTH -> SHAPE_WEST;
            case WEST -> SHAPE_NORTH;
            default -> SHAPE_EAST;
        };
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return this.getShape(state, level, pos, CollisionContext.empty());
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
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

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (newState.getBlock() instanceof CopperStoveBlock) {
                return;
            }
            if (level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity entity) {
                entity.dropContent();
            }
            super.onRemove(state, level, pos, newState, isMoving);
            level.updateNeighbourForOutputSignal(pos, this);
            return;
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof CopperStoveBlockEntity entity && !entity.isRemoved()) {
            Direction blockDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            ItemStack stack = player.getItemInHand(hand);

            if (!stack.isEmpty()) {
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    entity.kindle();
                } else if (hit.getLocation().y - hit.getBlockPos().getY() >= 0.9375) {
                    player.setItemInHand(hand, entity.items.insertItem(getIndexFromHitResult(hit, blockDir), stack, false));
                } else {
                    player.setItemInHand(hand, entity.fuels.insertItem(stack, false));
                }
                return InteractionResult.SUCCESS;
            } else {
                if (hit.getLocation().y - hit.getBlockPos().getY() >= 0.9375) {
                    ItemStack extract = entity.items.extractItem(getIndexFromHitResult(hit, blockDir), Item.MAX_STACK_SIZE, false);
                    for (SmeltingRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)) {
                        if (recipe.getResultItem(level.registryAccess()).is(extract.getItem())) {
                            player.awardRecipes(java.util.Collections.singleton(recipe));
                        }
                    }
                    player.setItemInHand(InteractionHand.MAIN_HAND, extract);
                } else {
                    player.setItemInHand(InteractionHand.MAIN_HAND, entity.fuels.extractItem(false));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, AllBlockEntity.copper_stove.get(), CopperStoveBlockEntity::clientTick);
        } else {
            return createTickerHelper(blockEntityType, AllBlockEntity.copper_stove.get(), CopperStoveBlockEntity::serverTick);
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return !state.getValue(LIT);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().inFire(), 2.0f);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockpos = hit.getBlockPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, blockpos)
                && level.getBlockEntity(hit.getBlockPos()) instanceof CopperStoveBlockEntity entity && !entity.isLit()) {
            entity.kindle();
        }
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

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double d0 = pos.getX() + 0.5;
            double d1 = pos.getY() + 0.75;
            double d2 = pos.getZ() + 0.5;

            if (random.nextInt(state.getValue(POWERED) ? 5 : 10) == 0) {
                level.playLocalSound(d0, d1, d2, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
                        0.5F + random.nextFloat(), random.nextFloat() * 0.8F + 1F, false);
            }

            for (int i = 0; i < random.nextInt(1) + 1; i++) {
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, random.nextFloat() / 2.0F, 5.0E-5, random.nextFloat() / 2.0F);
            }

            for (int i = 0; i < random.nextIntBetweenInclusive(4, 6); i++) {
                level.addAlwaysVisibleParticle(ParticleTypes.SMOKE,
                        d0 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        d1 + random.nextDouble() * 0.5,
                        d2 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }
    }

    protected static int getIndexFromHitResult(BlockHitResult result, Direction direction) {
        Vec3 hitLoc = result.getLocation();
        BlockPos pos = result.getBlockPos();
        double relX = hitLoc.x - pos.getX();
        double relZ = hitLoc.z - pos.getZ();
        boolean b0 = relX > 0.5d;
        boolean b1 = relZ > 0.5d;
        int index = b1 ? (b0 ? 2 : 3) : (b0 ? 1 : 0);
        int dir = direction.get2DDataValue();
        return (index - dir + 4) % 4;
    }
}
