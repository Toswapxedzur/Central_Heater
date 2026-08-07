package com.minecart.central_heater.block.misc;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.misc.AshtrayBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;

public class AshtrayBlock extends BaseEntityBlock {
    public static final MapCodec<AshtrayBlock> CODEC = simpleCodec(AshtrayBlock::new);
    public static final IntegerProperty ASH_LEVEL = IntegerProperty.create("ash_level", 0, 6);
    public static final EnumProperty<AshType> ASH_TYPE = EnumProperty.create("ash_type", AshType.class);
    public static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box((double)0.0F, (double)0.0F, (double)4.0F, (double)16.0F, (double)3.0F, (double)12.0F), new VoxelShape[]{box((double)4.0F, (double)0.0F, (double)0.0F, (double)12.0F, (double)3.0F, (double)16.0F), box((double)2.0F, (double)0.0F, (double)2.0F, (double)14.0F, (double)3.0F, (double)14.0F), box((double)2.0F, (double)4.0F, (double)2.0F, (double)14.0F, (double)16.0F, (double)14.0F)}), BooleanOp.ONLY_FIRST);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }

    public AshtrayBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ASH_LEVEL, 0)
                .setValue(ASH_TYPE, AshType.NORMAL));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AshtrayBlockEntity ashtray) {
            if (AshtrayBlockEntity.isAsh(stack)) {
                if (!level.isClientSide) {
                    // Try to insert the whole stack. The QueueHandler handles the limits and rejects mismatches!
                    ItemStack remainder = ashtray.inventory.insertItem(stack, false);
                    player.setItemInHand(hand, remainder);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // 2. Handle clicking WITHOUT an item (Extract)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AshtrayBlockEntity ashtray) {
            if (!level.isClientSide) {
                // Extract 1 ash at a time
                ItemStack extracted = ashtray.inventory.extractItem(1, false);
                if (!extracted.isEmpty()) {
                    // Give to player, or drop on ground if inventory is full
                    if (!player.getInventory().add(extracted)) {
                        player.drop(extracted, false);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        // Only drop items if the block is actually being destroyed/replaced by a different block
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AshtrayBlockEntity ashtray) {
                // Loop through your QueueItemStackHandler and drop everything
                for (int i = 0; i < ashtray.getInventory().getSlots(); i++) {
                    ItemStack stack = ashtray.getInventory().getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        // popResource spawns the item entity naturally in the world
                        Block.popResource(level, pos, stack);
                    }
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    // 3. Link the BlockEntity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AshtrayBlockEntity(pos, state);
    }

    // 4. Register the Ticker so the block updates every tick
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, AllBlockEntity.ASHTRAY.get(), AshtrayBlockEntity::tick);
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
