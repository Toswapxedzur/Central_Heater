package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.block.GoldenStoveBlock;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.fuel.FuelMapHook;
import com.minecart.central_heater.util.NetherFireState;
import com.minecart.central_heater.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GoldenStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;
    public NetherFireState litState;
    public NetherFireState prevLitState;

    public int[] cookingProgress;
    public int[] smeltingTotalTime;
    public int[] seethingTotalTime;
    public NonNullList<ItemStack> prevItems;

    public static final int fuelConsumptionRate = 3;
    public static final int netherFuelConsumptionRate = 7;
    public static final int coolRate = 2;
    public static final float processMultiplier = 2f;

    public GoldenStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.red_nether_brick_stove_be.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0 || FuelMapHook.getBurnTime(stack) != 0, 9);
        litState = NetherFireState.NONE;
        litTime = 0;
        prevLitState = NetherFireState.NONE;
        cookingProgress = new int[itemCapacity];
        smeltingTotalTime = new int[itemCapacity];
        seethingTotalTime = new int[itemCapacity];
        prevItems = NonNullList.withSize(itemCapacity, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        litTime = tag.getInt("litTime");
        litState = NetherFireState.func.apply(tag.getString("litState"));
        prevLitState = NetherFireState.func.apply(tag.getString("litStateValidator"));
        cookingProgress = tag.getIntArray("cookingProgress");
        smeltingTotalTime = tag.getIntArray("cookingTotalTime");
        seethingTotalTime = tag.getIntArray("seethingTotalTime");
        ContainerHelper.loadAllItems(tag.getCompound("validator"), prevItems, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("litTime", litTime);
        tag.putString("litState", litState.getSerializedName());
        tag.putString("litStateValidator", prevLitState.getSerializedName());
        tag.putIntArray("cookingProgress", cookingProgress);
        tag.putIntArray("cookingTotalTime", smeltingTotalTime);
        tag.putIntArray("seethingTotalTime", seethingTotalTime);
        CompoundTag validatorTag = new CompoundTag();
        ContainerHelper.saveAllItems(validatorTag, prevItems, registries);
        tag.put("validator", validatorTag);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.golden_stove");
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity) {

        if(entity.litState.equals(NetherFireState.SOUL))
            entity.litTime -= netherFuelConsumptionRate;
        else
            entity.litTime -= fuelConsumptionRate;
        if(entity.litTime <= 0){
            entity.litState = NetherFireState.NONE;
            entity.litTime = 0;
            if(!entity.prevLitState.equals(NetherFireState.NONE))
                entity.burnOneFuel();
        }
        entity.prevLitState = entity.litState;

        for (int i = 0; i < entity.itemCapacity; i++) {
            if(!entity.isLit())
                entity.cookingProgress[i] = Math.max(0, entity.cookingProgress[i] - coolRate);
            if(ItemStack.matches(entity.items.getStackInSlot(i), entity.prevItems.get(i))) {
                if(entity.isLit())
                    entity.cookingProgress[i] += 1;
            }else if(ItemStack.isSameItemSameComponents(entity.items.getStackInSlot(i), entity.prevItems.get(i))){
                entity.smeltingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, entity.items.getStackInSlot(i), processMultiplier);
                entity.seethingTotalTime[i] = RecipeUtil.getCookTime(level, AllRecipe.SEETHING.get(), entity.items.getStackInSlot(i), processMultiplier);
            }else{
                entity.cookingProgress[i] = 0;
                entity.smeltingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, entity.items.getStackInSlot(i), processMultiplier);
                entity.seethingTotalTime[i] = RecipeUtil.getCookTime(level, AllRecipe.SEETHING.get(), entity.items.getStackInSlot(i), processMultiplier);
            }
            if(!entity.litState.equals(entity.prevLitState) && !entity.prevLitState.equals(NetherFireState.SOUL)) {
                entity.cookingProgress[i] = 0;
            }
            entity.prevItems.set(i, entity.items.getStackInSlot(i).copy());
        }

        for (int i = 0; i < entity.itemCapacity; i++) {
            ItemStack ingredient = entity.items.getStackInSlot(i);
            ItemStack result;
            if (entity.smeltingTotalTime[i] != 0 && entity.cookingProgress[i] >= entity.smeltingTotalTime[i]) {
                result = RecipeUtil.getCookResult(RecipeType.SMELTING, ingredient);
                result.setCount(ingredient.getCount());
                if(!result.isEmpty())
                    entity.items.setStackInSlot(i, result);
            }
            else if (entity.seethingTotalTime[i] != 0 && entity.cookingProgress[i] >= entity.seethingTotalTime[i]) {
                result = RecipeUtil.getCookResult(AllRecipe.SEETHING.get(), ingredient);
                result.setCount(ingredient.getCount());
                if(!result.isEmpty())
                    entity.items.setStackInSlot(i, result);
            }
        }

        if(!state.getValue(AllConstants.LIT_SOUL).equals(entity.litState)){
            entity.updateBlockState(entity.getBlockState().setValue(GoldenStoveBlock.LIT_SOUL, entity.litState));
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity){
        RandomSource randomsource = level.random;
        if(!state.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.NONE)) {
            if (randomsource.nextFloat() < 0.11F) {
                for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,true,
                            (double)pos.getX() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            (double)pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                            (double)pos.getZ() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            0.0, 0.07, 0.0);
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void dropContent() {
        for(int i = 0; i< fuels.getSlots(); i++)
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+0.5, this.getBlockPos().getZ()+0.5, this.fuels.getStackInSlot(i)));
        for(int i = 0; i< items.getSlots(); i++)
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+0.8, this.getBlockPos().getZ()+0.5, this.items.getStackInSlot(i)));
    }

    public void kindle() {
        if(this.isLit())
            return;
        burnOneFuel();
    }

    public boolean isLit() { return !this.litState.equals(NetherFireState.NONE); }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0 && FuelMapHook.getBurnTime(stack) == 0 )
            return;
        stack = fuels.extractItem(false);
        if(FuelMapHook.getBurnTime(stack) != 0){
            this.litState = NetherFireState.SOUL;
            this.litTime += FuelMapHook.getBurnTime(stack);
        }else{
            this.litState = NetherFireState.BURN;
            this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        }
        if (stack.hasCraftingRemainingItem()) {
            fuels.insertItem(stack.getCraftingRemainingItem(), false);
        }
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }

    public void updateBlockEntity(){
        setChanged(getLevel(), this.getBlockPos(), getBlockState());
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

}
