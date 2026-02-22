package com.minecart.central_heater.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;

import java.util.HashMap;
import java.util.Map;

public class BurntLogBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 4);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<BlockState, VoxelShape> SHAPES = new HashMap<>();

    public BurntLogBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 4).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
        generateShapes();
    }

    private void generateShapes() {
        VoxelShape[] baseShapes = new VoxelShape[]{
                Block.box(0, 0, 0, 16, 4, 16),
                Block.box(0, 0, 0, 16, 8, 16),
                Block.box(0, 0, 0, 16, 12, 16),
                Block.box(0, 0, 0, 16, 16, 16)
        };

        for (Direction direction : Direction.values()) {
            for (int i = 1; i <= 4; i++) {
                VoxelShape shape = baseShapes[i - 1];

                if (direction == Direction.DOWN) shape = Block.box(0, 16 - (i * 4), 0, 16, 16, 16);
                else if (direction == Direction.NORTH) shape = Block.box(0, 0, 16 - (i * 4), 16, 16, 16);
                else if (direction == Direction.SOUTH) shape = Block.box(0, 0, 0, 16, 16, i * 4);
                else if (direction == Direction.WEST) shape = Block.box(16 - (i * 4), 0, 0, 16, 16, 16);
                else if (direction == Direction.EAST) shape = Block.box(0, 0, 0, i * 4, 16, 16);

                BlockState stateKey = this.defaultBlockState().setValue(FACING, direction).setValue(LAYERS, i);
                BlockState stateKeyWaterLogged = this.defaultBlockState().setValue(WATERLOGGED, true).setValue(FACING, direction).setValue(LAYERS, i);
                SHAPES.put(stateKey, shape);
                SHAPES.put(stateKeyWaterLogged, shape);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.getOrDefault(state, Block.box(0, 0, 0, 16, 16, 16));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(LAYERS, 4).setValue(WATERLOGGED, false);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return state.getValue(LAYERS) <= 3 ? SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState) : false;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return state.getValue(LAYERS) <= 3 && (!state.getValue(FACING).equals(Direction.UP) || state.getValue(LAYERS) <= 2) ? SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluid) : false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.canPerformAction(ToolActions.AXE_STRIP)) {
            if (!level.isClientSide) {
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                heldItem.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));

                if (level.random.nextFloat() < 0.25F) {
                    popResource(level, pos, new ItemStack(Items.CHARCOAL));
                }

                int currentLayers = state.getValue(LAYERS);
                if (currentLayers > 1) {
                    level.setBlock(pos, state.setValue(LAYERS, currentLayers - 1), 3);
                } else {
                    level.destroyBlock(pos, false);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS, FACING, WATERLOGGED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(LAYERS) < 4;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        switch (type) {
            case LAND:
                return state.getValue(DirectionalBlock.FACING).equals(Direction.DOWN) || state.getValue(DirectionalBlock.FACING).equals(Direction.UP) && state.getValue(LAYERS) == 4;
            case WATER:
                return state.getFluidState().is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
}