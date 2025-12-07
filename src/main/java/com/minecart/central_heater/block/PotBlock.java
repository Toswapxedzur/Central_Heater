package com.minecart.central_heater.block;

import com.minecart.central_heater.AllBlockEntity;
import com.minecart.central_heater.block_entity.pot.AbstractPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public abstract class PotBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    protected PotBlock(Properties properties) {
        super(properties.noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LEVEL)));
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LEVEL, Integer.valueOf(0)));
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
        if(level.getBlockEntity(pos) instanceof AbstractPotBlockEntity entity && !entity.isRemoved()){
            if(stack.isEmpty()){
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            else if(FluidUtil.tryEmptyContainer(stack, entity.getFluidTank(), 1000, player, false).isSuccess()){
                player.setItemInHand(hand, FluidUtil.tryEmptyContainer(stack, entity.getFluidTank(), 1000, player, true).getResult());
            }
            else if(FluidUtil.tryFillContainer(stack, entity.getFluidTank(), 1000, player, false).isSuccess()){
                player.setItemInHand(hand, FluidUtil.tryFillContainer(stack, entity.getFluidTank(), 1000, player, true).getResult());
            }
            else if(!ItemStack.matches(stack, entity.getContainer().insertItem(stack, true))){
                player.setItemInHand(hand, entity.getContainer().insertItem(stack, false));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof AbstractPotBlockEntity entity && !level.getBlockEntity(pos).isRemoved()){
            player.setItemInHand(InteractionHand.MAIN_HAND, entity.getContainer().extractItem(false));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
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
            if (level.getBlockEntity(pos) instanceof AbstractPotBlockEntity entity)
                entity.drop();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }
}
