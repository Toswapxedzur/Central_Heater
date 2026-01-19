package com.minecart.central_heater.item.complex_items;

import com.minecart.central_heater.entity.MinecartBlazingFurnace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class BlazingFurnaceMinecartItem extends Item {
    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            ServerLevel level = source.getLevel();

            // 1.20.1: BlockSource extends Position, so x(), y(), z() are available directly
            double x = source.x() + (double)direction.getStepX() * 1.125F;
            double y = Math.floor(source.y()) + (double)direction.getStepY();
            double z = source.z() + (double)direction.getStepZ() * 1.125F;

            BlockPos blockpos = source.getPos().relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);

            // Forge 1.20.1 Rail Logic
            RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock rail
                    ? rail.getRailDirection(blockstate, level, blockpos, null)
                    : RailShape.NORTH_SOUTH;

            double yOffset;

            if (blockstate.is(BlockTags.RAILS)) {
                if (railshape.isAscending()) {
                    yOffset = 0.6;
                } else {
                    yOffset = 0.1;
                }
            } else {
                if (!blockstate.isAir() || !level.getBlockState(blockpos.below()).is(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(source, stack);
                }

                BlockState blockstateBelow = level.getBlockState(blockpos.below());
                RailShape railshapeBelow = blockstateBelow.getBlock() instanceof BaseRailBlock railBelow
                        ? railBelow.getRailDirection(blockstateBelow, level, blockpos.below(), null)
                        : RailShape.NORTH_SOUTH;

                if (direction != Direction.DOWN && railshapeBelow.isAscending()) {
                    yOffset = -0.4;
                } else {
                    yOffset = -0.9;
                }
            }

            MinecartBlazingFurnace cart = new MinecartBlazingFurnace(level, x, y + yOffset, z);

            level.addFreshEntity(cart);

            stack.shrink(1);
            return stack;
        }

        @Override
        protected void playSound(BlockSource source) {
            source.getLevel().levelEvent(1000, source.getPos(), 0);
        }
    };

    public BlazingFurnaceMinecartItem(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);

        if (!blockstate.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemstack = context.getItemInHand();
            if (level instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)level;

                RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock rail
                        ? rail.getRailDirection(blockstate, level, blockpos, null)
                        : RailShape.NORTH_SOUTH;

                double yOffset = 0.0D;
                if (railshape.isAscending()) {
                    yOffset = 0.5D;
                }

                MinecartBlazingFurnace cart = new MinecartBlazingFurnace(
                        serverlevel,
                        (double)blockpos.getX() + 0.5D,
                        (double)blockpos.getY() + 0.0625D + yOffset,
                        (double)blockpos.getZ() + 0.5D
                );

                serverlevel.addFreshEntity(cart);
                serverlevel.gameEvent(GameEvent.ENTITY_PLACE, blockpos, GameEvent.Context.of(context.getPlayer(), serverlevel.getBlockState(blockpos.below())));
            }

            itemstack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}