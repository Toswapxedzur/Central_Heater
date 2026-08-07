package com.minecart.central_heater.block.stove;

import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import com.minecart.central_heater.block.stove.CopperStoveBlock;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;
import com.minecart.central_heater.heat.api.ThermalOverrideProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStoveBlock extends BaseEntityBlock implements ThermalOverrideProvider {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    // The center carved-out area is symmetrical, so we can reuse it for all 4 directions!
    private static final VoxelShape CENTER_HOLE = box(3, 3, 3, 13, 14, 13);

    // Pre-calculated shapes for each direction
    public static final VoxelShape SHAPE_NORTH = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(0, 5, 3, 3, 12, 13)), BooleanOp.ONLY_FIRST);

    public static final VoxelShape SHAPE_EAST = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(3, 5, 0, 13, 12, 3)), BooleanOp.ONLY_FIRST);

    public static final VoxelShape SHAPE_SOUTH = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(13, 5, 3, 16, 12, 13)), BooleanOp.ONLY_FIRST);

    public static final VoxelShape SHAPE_WEST = Shapes.join(Shapes.block(),
            Shapes.or(CENTER_HOLE, box(3, 5, 13, 13, 12, 16)), BooleanOp.ONLY_FIRST);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_SOUTH;
            case SOUTH -> SHAPE_WEST;
            case WEST -> SHAPE_NORTH;
            default -> SHAPE_EAST; // Fallback to NORTH
        };
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        // Make sure your interaction shape matches so players can click it correctly!
        return this.getShape(state, level, pos, CollisionContext.empty());
    }

    protected AbstractStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ThermalMaterial getThermalMaterialOverride(BlockState state) {
        if (this instanceof CopperStoveBlock) {
            return ThermalMaterial.COPPER;
        }
        if (this instanceof GoldenStoveBlock) {
            return ThermalMaterial.GOLD;
        }
        String path = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(this).getPath();
        if (path.contains("brick") || path.contains("blackstone") || path.contains("mud")) {
            return ThermalMaterial.BRICK;
        }
        if (path.contains("deepslate")) {
            return ThermalMaterial.DEEPSLATE;
        }
        return ThermalMaterial.STONE;
    }

    @Override
    public @Nullable ThermalMaterialBehavior getThermalBehaviorOverride(BlockState state) {
        return null;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected int getIndexFromHitResult(BlockHitResult result, Direction direction) {
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

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        // Generalized to the Abstract Block Entity so all 3 tiers can use this!
        if (!player.getItemInHand(hand).isEmpty() && level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity entity && !entity.isRemoved()) {
            Direction blockDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (stack.is(Items.FLINT_AND_STEEL)) {
                entity.kindle();
            } else if (hitResult.getLocation().y - hitResult.getBlockPos().getY() >= 0.9375) {
                player.setItemInHand(hand, entity.items.insertItem(getIndexFromHitResult(hitResult, blockDir), stack, false));
            } else {
                player.setItemInHand(hand, entity.fuels.insertItem(stack, false));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.isClientSide) return;
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity entity)
                entity.dropContent();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockpos = hit.getBlockPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, blockpos) &&
                level.getBlockEntity(hit.getBlockPos()) instanceof AbstractStoveBlockEntity entity && !entity.isLit()) {
            entity.kindle();
        }
    }
}
