package com.minecart.central_heater.block.misc;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.misc.AshtrayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AshtrayBlock extends BaseEntityBlock {
    public static final IntegerProperty ASH_LEVEL = IntegerProperty.create("ash_level", 0, 6);
    public static final EnumProperty<AshType> ASH_TYPE = EnumProperty.create("ash_type", AshType.class);

    public static final VoxelShape SHAPE = Shapes.join(Shapes.block(),
            Shapes.or(
                    box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
                    box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0),
                    box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
                    box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)
            ),
            BooleanOp.ONLY_FIRST);

    public AshtrayBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ASH_LEVEL, 0)
                .setValue(ASH_TYPE, AshType.NORMAL));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ASH_LEVEL, ASH_TYPE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof AshtrayBlockEntity ashtray) {
            ItemStack stack = player.getItemInHand(hand);

            if (!stack.isEmpty() && AshtrayBlockEntity.isAsh(stack)) {
                if (!level.isClientSide) {
                    ItemStack remainder = ashtray.inventory.insertItem(stack, false);
                    player.setItemInHand(hand, remainder);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (stack.isEmpty()) {
                if (!level.isClientSide) {
                    ItemStack extracted = ashtray.inventory.extractItem(1, false);
                    if (!extracted.isEmpty()) {
                        if (!player.getInventory().add(extracted)) {
                            player.drop(extracted, false);
                        }
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AshtrayBlockEntity ashtray) {
                for (int i = 0; i < ashtray.getInventory().getSlots(); i++) {
                    ItemStack stack = ashtray.getInventory().getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        Block.popResource(level, pos, stack);
                    }
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AshtrayBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, AllBlockEntity.ashtray.get(), AshtrayBlockEntity::tick);
    }

    public enum AshType implements StringRepresentable {
        NORMAL("normal"),
        SCORCHED("scorched");

        private final String name;

        AshType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
