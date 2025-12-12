package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.AllBlockEntity;
import com.minecart.central_heater.block.BrickStoveBlock;
import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.util.FireState;
import com.minecart.central_heater.util.RecipeUtil;
import net.minecraft.core.*;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class StoneStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;
    public FireState litState;
    public FireState prevLitState;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> prevItems;

    public static final int fuelConsumptionRate = 2;
    public static final int coolRate = 2;
    public static final float processMultiplier = 1f;

    public StoneStoveBlockEntity(BlockPos pos, BlockState blockState){
        super(AllBlockEntity.stone_stove.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0, 4);
        litState = FireState.NONE;
        litTime = 0;
        prevLitState = FireState.NONE;
        cookingProgress = new int[getItemSlots()];
        cookingTotalTime = new int[getItemSlots()];
        prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.litState = FireState.getFireState(tag.getBoolean("litState"));
        this.litTime = tag.getInt("LitTime");
        this.prevLitState = FireState.getFireState(tag.getBoolean("prevLitState"));
        this.cookingProgress = tag.getIntArray("cookingProgress");
        this.cookingTotalTime = tag.getIntArray("cookingTotalTime");
        ContainerHelper.loadAllItems(tag.getCompound("prevItems"), prevItems, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("litState", this.litState.getBooleanState());
        tag.putInt("LitTime", this.litTime);
        tag.putBoolean("prevLitState", this.prevLitState.getBooleanState());
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
        CompoundTag prevItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(prevItemsTag, prevItems, registries);
        tag.put("prevItems", prevItemsTag);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StoneStoveBlockEntity entity) {
        if(level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN))
            entity.dropContent();

        entity.litTime -= fuelConsumptionRate;
        if(entity.litTime<=0){
            entity.litState = FireState.NONE;
            entity.litTime = 0;
            if(entity.prevLitState.equals(FireState.LIT))
                entity.burnOneFuel();
        }
        entity.prevLitState = entity.litState;

        for (int i = 0; i < entity.getItemSlots(); i++) {
            if(!entity.isLit())
                entity.cookingProgress[i] = Math.max(0, entity.cookingProgress[i] - coolRate);
            if(ItemStack.matches(entity.items.getStackInSlot(i), entity.prevItems.get(i))) {
                if(entity.isLit())
                    entity.cookingProgress[i] += 1;
            }else if(ItemStack.isSameItemSameComponents(entity.items.getStackInSlot(i), entity.prevItems.get(i))){
                entity.cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, entity.items.getStackInSlot(i), processMultiplier);
            }else{
                entity.cookingProgress[i] = 0;
                entity.cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, entity.items.getStackInSlot(i), processMultiplier);
            }
            entity.prevItems.set(i, entity.items.getStackInSlot(i).copy());
        }

        for (int i = 0; i < entity.getItemSlots(); i++) {
            if (entity.cookingTotalTime[i] != 0 && entity.cookingProgress[i] >= entity.cookingTotalTime[i]) {
                ItemStack ingredient = entity.items.getStackInSlot(i);
                ItemStack result = RecipeUtil.getCookResult(RecipeType.SMELTING, ingredient);
                result.setCount(ingredient.getCount());
                if(!result.isEmpty())
                    entity.items.setStackInSlot(i, result);
            }
        }

        if(state.getValue(StoneStoveBlock.LIT) != entity.isLit()){
            entity.updateBlockState(entity.getBlockState().setValue(StoneStoveBlock.LIT, entity.isLit()));
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, StoneStoveBlockEntity entity){
        RandomSource randomsource = level.random;
        if(state.getValue(StoneStoveBlock.LIT).booleanValue()) {
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

        if(state.getValue(StoneStoveBlock.LIT).booleanValue()) {
            int facingValue = state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue();

            for (int j = 0; j < entity.items.getSlots(); ++j) {

                if (!entity.items.getStackInSlot(j).isEmpty() && randomsource.nextFloat() < 0.2F) {

                    Direction direction = Direction.from2DDataValue(Math.floorMod(j + facingValue, 4));
                    float offset = 0.3125F;

                    double x = (double) pos.getX() + 0.5D
                            - (double) ((float) direction.getStepX() * offset)
                            + (double) ((float) direction.getClockWise().getStepX() * offset);

                    double y = (double) pos.getY() + 1.0D;

                    double z = (double) pos.getZ() + 0.5D
                            - (double) ((float) direction.getStepZ() * offset)
                            + (double) ((float) direction.getClockWise().getStepZ() * offset);

                    for (int k = 0; k < 4; ++k) {
                        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }
    }

    public void dropContent() {
        for(int i = 0; i< fuels.getSlots(); i++) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, this.fuels.getStackInSlot(i)));
            fuels.setStackInSlot(i, ItemStack.EMPTY);
        }
        for(int i = 0; i< items.getSlots(); i++) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.8, this.getBlockPos().getZ() + 0.5, this.items.getStackInSlot(i)));
            items.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void kindle() {
        if(this.isLit())
            return;
        burnOneFuel();
    }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0)
            return;
        stack = fuels.extractItem(false);
        this.litState = FireState.LIT;
        this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        if (stack.hasCraftingRemainingItem()) {
            fuels.insertItem(stack.getCraftingRemainingItem(), false);
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

    public boolean isLit() { return this.litState.equals(FireState.LIT); }

    public void updateBlockEntity(){
        setChanged(getLevel(), this.getBlockPos(), getBlockState());
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }
}
