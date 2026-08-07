package com.minecart.central_heater.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class QueueItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    protected NonNullList<ItemStack> stacks;
    public int maxSlotLimit;

    public QueueItemStackHandler(int size, int maxSlotLimit){
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.maxSlotLimit = maxSlotLimit;
    }

    public QueueItemStackHandler(int size){
        this(size, 1);
    }

    public QueueItemStackHandler(NonNullList<ItemStack> stacks, int maxSlotLimit){
        this.stacks = stacks;
        this.maxSlotLimit = maxSlotLimit;
    }

    public QueueItemStackHandler(NonNullList<ItemStack> stacks){
        this(stacks, 1);
    }

    public QueueItemStackHandler(){
        this(1);
    }

    public NonNullList<ItemStack> get(){
        return this.stacks;
    }

    public void set(NonNullList<ItemStack> stacks){
        this.stacks = stacks;
    }

    /**
     *
     * @param size
     * Cleans all itemstack and set a new size for the itemstack handler
     */
    public void setSize(int size){
        set(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    /**
     *
     * @return The number of slots
     */
    public int getSlots() {
        return stacks.size();
    }

    /**
     *
     * @return The number of itemstacks
     */
    public int getQueueSize(){
        return (int) stacks.stream().filter(itemstack -> !itemstack.isEmpty()).count();
    }

    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return stacks.get(slot);
    }

    public ItemStack getLastStack() {
        if(getQueueSize() > 0)
            return getStackInSlot(getQueueSize() - 1);
        return ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, ItemStack stack){
        validateSlotIndex(slot);
        stacks.set(slot, stack);
        onContentsChanged();
    }

    /**
     *
     * @return The maximun amount of item any slots can hold
     */
    public int getSlotLimit() { return maxSlotLimit; }

    @Override
    public int getSlotLimit(int slot) {
        return getSlotLimit();
    }

    public int getStackLimit(ItemStack stack) {
        return Math.min(getSlotLimit(), stack.getMaxStackSize());
    }

    /**
     * Can be overriden to only allow certain items
     */
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return isItemValid(stack);
    }

    /**
     *
     * @param stack
     * @param simulate whether the action will be perform
     * <br>
     * Insert a itemstack into the handler
     * @return the leftover itemstack after insertion
     */
    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        for(int i=0;i<getSlots();i++){
            ItemStack simulatedRemainder = insertItem(i, stack, true);
            if(simulatedRemainder.getCount() != stack.getCount()){
                if(simulate)
                    return simulatedRemainder;
                return insertItem(i, stack, false);
            }
        }

        return stack;
    }

    /**
     *
     * @param amount The amount to extract
     * @param simulate Whether the action will be performed
     * <br>
     * Extract one itemstack from the handler
     * @return the extracted itemstack
     */
    public ItemStack extractItem(int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        for(int i=0;i<getSlots();i++){
            ItemStack result = extractItem(i, amount, true);
            if (!result.isEmpty()) {
                if (simulate) {
                    return result;
                } else {
                    result = extractItem(i, amount, false);
                    pop();
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public ItemStack extractItem(boolean simulate){
        return extractItem(Item.ABSOLUTE_MAX_STACK_SIZE, simulate);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        int limit = getStackLimit(stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            pop();
            onContentsChanged();
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, ItemStack.EMPTY);
                pop();
                onContentsChanged();
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
                pop();
                onContentsChanged();
            }

            return existing.copyWithCount(toExtract);
        }
    }

    public void pop(){
        boolean changed = false;
        int toSwap = 0;
        for(int i=0;i<getSlots();i++){
            ItemStack stack = getStackInSlot(i);
            if(!stack.isEmpty()){
                if(i != toSwap){
                    setStackInSlot(toSwap, stack);
                    setStackInSlot(i, ItemStack.EMPTY);
                    changed = true;
                }
                toSwap++;
            }
        }
        if(changed)
            onContentsChanged();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                nbtTagList.add(stacks.get(i).save(provider, itemTag));
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                ItemStack.parse(provider, itemTags).ifPresent(stack -> stacks.set(slot, stack));
            }
        }
        onLoad();
    }

    protected void onLoad() {}

    protected void onContentsChanged() {}
}
