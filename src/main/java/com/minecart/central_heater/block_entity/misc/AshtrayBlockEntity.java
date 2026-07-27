package com.minecart.central_heater.block_entity.misc;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.block.misc.AshtrayBlock;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import com.minecart.central_heater.capability.QueueItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import java.util.ArrayList;
import java.util.List;

public class AshtrayBlockEntity extends BlockEntity {

    public QueueItemStackHandler getInventory() {
        return inventory;
    }

    public final QueueItemStackHandler inventory = new QueueItemStackHandler(6, 1) {
        @Override
        public boolean isItemValid(ItemStack stack) {
            return isAsh(stack);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                ItemStack stack = getLastStack();
                int count = getQueueSize();
                AshtrayBlock.AshType type = AshtrayBlock.AshType.NORMAL;

                // Determine the correct AshType enum based on the item
                if (count > 0) {
                    if (stack.is(AllBlockItem.SCORCHED_DUST.get())) {
                        type = AshtrayBlock.AshType.SCORCHED;
                    } else if (stack.is(AllBlockItem.FIRE_ASH.get())) {
                        type = AshtrayBlock.AshType.NORMAL;
                    }
                }

                BlockState currentState = getBlockState();

                if (currentState.getValue(AshtrayBlock.ASH_LEVEL) != count || currentState.getValue(AshtrayBlock.ASH_TYPE) != type) {
                    level.setBlock(getBlockPos(),
                            currentState.setValue(AshtrayBlock.ASH_LEVEL, count)
                                    .setValue(AshtrayBlock.ASH_TYPE, type),
                            3);
                }
            }
        }
    };

    public AshtrayBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntity.ASHTRAY.get(), pos, state);
    }

    public static boolean isAsh(ItemStack stack) {
        return stack.is(AllBlockItem.FIRE_ASH.get()) || stack.is(AllBlockItem.SCORCHED_DUST.get());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AshtrayBlockEntity entity) {
        if (level.isClientSide) return;

        // 1. Absorb dropped items sitting inside the bowl of the ashtray
        // We define a box slightly smaller than the block to represent the hollow inside
        AABB innerBounds = new AABB(pos.getX() + 0.125, pos.getY() + 0.1875, pos.getZ() + 0.125,
                pos.getX() + 0.875, pos.getY() + 1.0, pos.getZ() + 0.875);

        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, innerBounds);
        for (ItemEntity itemEntity : items) {
            ItemStack stack = itemEntity.getItem();
            if (isAsh(stack)) {
                ItemStack remainder = entity.inventory.insertItem(stack, false);
                if (remainder.isEmpty()) {
                    itemEntity.discard(); // Sucked up completely
                } else {
                    itemEntity.setItem(remainder); // Sucked up partially
                }
            }
        }

        if (level.getGameTime() % 4 == 0) {
            if (entity.inventory.getStackInSlot(0).getCount() >= 6) return;

            BlockEntity aboveBE = level.getBlockEntity(pos.above());
            if (aboveBE != null) {
                IItemHandler targetHandler = null;

                // Check our registered interfaces
                for (IAshSourceProvider provider : ASH_SOURCES) {
                    targetHandler = provider.getAshHandler(aboveBE);
                    if (targetHandler != null) break;
                }

                if (targetHandler != null) {
                    // Loop through the exposed handler to find and extract ash
                    for (int i = 0; i < targetHandler.getSlots(); i++) {
                        ItemStack stackInSlot = targetHandler.getStackInSlot(i);
                        if (isAsh(stackInSlot)) {
                            ItemStack extracted = targetHandler.extractItem(i, 1, true);
                            if (!extracted.isEmpty()) {
                                ItemStack remainder = entity.inventory.insertItem(extracted, true);
                                if (remainder.isEmpty()) {
                                    targetHandler.extractItem(i, 1, false);
                                    entity.inventory.insertItem(extracted, false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory", 10)) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }

    // --- ASH SOURCE PROVIDER PATTERN ---

    @FunctionalInterface
    public interface IAshSourceProvider {
        //the block entity containing potential fuel slots
        IItemHandler getAshHandler(BlockEntity blockEntity);
    }

    public static final List<IAshSourceProvider> ASH_SOURCES = new ArrayList<>();

    public static void registerAshSource(IAshSourceProvider provider) {
        ASH_SOURCES.add(provider);
    }

    static {
        registerAshSource((be) -> {
            if (be instanceof AbstractFurnaceBlockEntity furnaceBe) {
                Level level = be.getLevel();
                if (level != null) {
                    return level.getCapability(Capabilities.ItemHandler.BLOCK, be.getBlockPos(), Direction.NORTH);
                }
            }
            return null;
        });

        registerAshSource((be) -> {
            if (be instanceof AbstractStoveBlockEntity stoveBe) {
                return stoveBe.fuels;
            }
            return null;
        });
    }
}