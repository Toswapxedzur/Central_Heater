package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.capability.QueueItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private Component name;
    public final QueueItemStackHandler fuels;
    public final ItemStackHandler items;

    public AbstractStoveBlockEntity(BlockEntityType<? extends AbstractStoveBlockEntity> type, BlockPos pos, BlockState blockState, int fuelCapacity, Predicate<ItemStack> isFuelValid, int itemCapacity) {
        super(type, pos, blockState);
        this.fuels = new QueueItemStackHandler(fuelCapacity, 1){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return isFuelValid.test(stack);
            }
            @Override
            protected void onContentsChanged() { updateBlockEntity(); }
        };
        this.items = new ItemStackHandler(itemCapacity){
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                updateBlockEntity();
            }
        };
    }

    public QueueItemStackHandler getFuels(){
        return fuels;
    }

    public ItemStackHandler getItems(){
        return items;
    }

    public int getFuelSlots(){
        return getFuels().getSlots();
    }

    public int getItemSlots(){
        return getItems().getSlots();
    }

    public ItemStack getStackInFuels(int i){
        return getFuels().getStackInSlot(i);
    }

    public ItemStack getStackInItems(int i){
        return getItems().getStackInSlot(i);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CustomName", 8)) {
            this.name = parseCustomNameSafe(tag.getString("CustomName"), registries);
        }
        items.deserializeNBT(registries, tag.getCompound("items"));
        fuels.deserializeNBT(registries, tag.getCompound("fuels"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name, registries));
        }
        tag.put("items", items.serializeNBT(registries));
        tag.put("fuels", fuels.serializeNBT(registries));
    }

    public Component getName() {
        return this.name != null ? this.name : Component.empty();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.name = (Component)componentInput.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.name);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
    }

    public abstract void updateBlockEntity();
}
