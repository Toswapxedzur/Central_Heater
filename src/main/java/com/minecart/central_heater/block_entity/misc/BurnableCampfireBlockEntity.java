package com.minecart.central_heater.block_entity.misc;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.capability.QueueItemStackHandler;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.mixin_interface.IAshProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class BurnableCampfireBlockEntity extends CampfireBlockEntity implements IAshProducer {
    public static final int fuel_slots = 2;
    public int litTime;
    public int campfireLitTime;
    public int ashCount;

    public final QueueItemStackHandler fuels = new QueueItemStackHandler(fuel_slots, 1){
        @Override
        public boolean isItemValid(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
        }

        @Override
        protected void onContentsChanged() {
            updateBlockEntity();
        }
    };

    public BurnableCampfireBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        litTime = 0;
        campfireLitTime = 200;
        ashCount = 0;
    }

    // Critical for custom BEs extending vanilla BEs:
    // Vanilla constructor forces type to CAMPFIRE, we must override it to our custom type
    // to match the block definition.
    @Override
    public BlockEntityType<?> getType() {
        return AllBlockEntity.burnable_campfire.get();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.litTime = tag.getInt("litTime");
        this.ashCount = tag.getInt("ashCount");

        if (tag.contains("campfireLitTime")) {
            this.campfireLitTime = tag.getInt("campfireLitTime");
        }

        if (tag.contains("fuels")) {
            this.fuels.deserializeNBT(tag.getCompound("fuels"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("litTime", litTime);
        tag.putInt("campfireLitTime", campfireLitTime);
        tag.put("fuels", fuels.serializeNBT());
        tag.putInt("ashCount", ashCount);
    }

    public int getLitTime() {
        return litTime;
    }

    public void setLitTime(int litTime) {
        this.litTime = litTime;
    }

    public int getCampfireLitTime(){
        return campfireLitTime;
    }

    public void setCampfireLitTime(int lit){
        campfireLitTime = lit;
    }

    public int getAshCount() {
        return ashCount;
    }

    @Override
    public void setAshCount(int count) {
        this.ashCount = count;
    }

    public ItemStack getFuel(int slot){
        return this.fuels.getStackInSlot(slot);
    }

    public void setFuel(int slot, ItemStack stack){
        this.fuels.setStackInSlot(slot, stack);
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public NonNullList<ItemStack> getFuels(){
        return this.fuels.get();
    }

    public boolean isLit(){
        return getBlockState().getValue(CampfireBlock.LIT);
    }

    public void setLit(boolean lit){
        if(level != null && !level.isClientSide)
            updateBlockState(getBlockState().setValue(CampfireBlock.LIT, lit));
    }

    public boolean addFuel(ItemStack stack, boolean simulate){
        for(int i=0; i<fuel_slots; i++){
            if(getFuel(i).isEmpty()){
                if(!simulate)
                    setFuel(i, stack.split(1));
                return true;
            }
        }
        return false;
    }

    public void burnFuel() {
        for (int i = fuel_slots - 1; i >= 0; i--) {
            ItemStack stack = getFuel(i);

            if (stack.isEmpty()) continue;

            int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
            if (burnTime <= 0) continue;

            // 1. Add Burn Time
            this.litTime += burnTime;

            // 2. Ash Drop Logic
            float chance = DataMapHook.getFireAshDropChance(stack);
            if (chance > 0 && this.level.random.nextFloat() < chance) {
                this.ashCount++;
            }

            // 3. Consume Fuel
            if (stack.hasCraftingRemainingItem()) {
                setFuel(i, stack.getCraftingRemainingItem().copy());
            } else {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    setFuel(i, ItemStack.EMPTY);
                }
            }

            // Fuel found and processed, exit loop
            return;
        }
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, BurnableCampfireBlockEntity blockEntity) {
        blockEntity.litTime -= 1;
        if (blockEntity.litTime <= 0) {
            blockEntity.burnFuel();
        }
        if(blockEntity.litTime < 0){
            blockEntity.campfireLitTime += blockEntity.litTime;
            blockEntity.litTime = 0;
        }
        if (blockEntity.campfireLitTime <= 0) {
            level.playLocalSound(pos, SoundEvents.GENERIC_BURN, SoundSource.BLOCKS, 1f, 1f, true);
            level.destroyBlock(pos, false);
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL);
            return;
        }

        CampfireBlockEntity.cookTick(level, pos, state, blockEntity);
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state, BurnableCampfireBlockEntity blockEntity) {
        blockEntity.litTime = 0;
        CampfireBlockEntity.cooldownTick(level, pos, state, blockEntity);
    }

    public void kindle(){
        if(!isLit())
            burnFuel();
        setLit(true);
    }

    public void dropContents(){
        if (level == null) return;
        Containers.dropContents(getLevel(), getBlockPos(), getFuels());
        Containers.dropContents(getLevel(), getBlockPos(), getItems());
        dropAsh(getLevel(), getBlockPos());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void clearContent() {
        this.fuels.get().clear();
        super.clearContent();
    }

    public void updateBlockEntity() {
        setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void updateBlockState(BlockState newState){
        if (level != null) {
            getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
        }
    }
}