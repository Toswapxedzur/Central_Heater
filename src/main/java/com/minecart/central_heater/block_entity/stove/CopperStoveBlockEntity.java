package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.block.stove.CopperStoveBlock;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.RecipeUtil;
import com.minecart.central_heater.misc.enumeration.FireState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

public class CopperStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;
    public FireState litState;
    public FireState prevLitState;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> prevItems;

    public static final int baseFuelConsumptionRate = 2;
    public static final int coolRate = 2;
    public static final float processMultiplier = 1.0f / 0.9f;

    public CopperStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.copper_stove.get(), pos, blockState, 4,
                stack -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0, 4, 2);
        litState = FireState.NONE;
        litTime = 0;
        prevLitState = FireState.NONE;
        cookingProgress = new int[getItemSlots()];
        cookingTotalTime = new int[getItemSlots()];
        prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
    }

    public int getFuelConsumptionRate() {
        BlockState state = getBlockState();
        if (!state.getValue(CopperStoveBlock.POWERED)) return baseFuelConsumptionRate;

        if (state.getBlock() instanceof CopperStoveBlock copperStove) {
            WeatheringCopper.WeatherState age = copperStove.getWeatherState();
            return switch (age) {
                case UNAFFECTED -> baseFuelConsumptionRate * 7;
                case EXPOSED -> baseFuelConsumptionRate * 5;
                case WEATHERED -> baseFuelConsumptionRate * 3;
                case OXIDIZED -> baseFuelConsumptionRate;
            };
        }
        return baseFuelConsumptionRate;
    }

    public int getCookingMultiplier() {
        BlockState state = getBlockState();
        if (!state.getValue(CopperStoveBlock.POWERED)) return 1;

        if (state.getBlock() instanceof CopperStoveBlock copperStove) {
            WeatheringCopper.WeatherState age = copperStove.getWeatherState();
            return switch (age) {
                case UNAFFECTED -> 4;
                case EXPOSED -> 3;
                case WEATHERED -> 2;
                case OXIDIZED -> 1;
            };
        }
        return 1;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.litState = FireState.getFireState(tag.getBoolean("litState"));
        this.litTime = tag.getInt("LitTime");
        this.prevLitState = FireState.getFireState(tag.getBoolean("prevLitState"));

        int[] progress = tag.getIntArray("cookingProgress");
        this.cookingProgress = (progress.length == getItemSlots()) ? progress : new int[getItemSlots()];

        int[] totalTime = tag.getIntArray("cookingTotalTime");
        this.cookingTotalTime = (totalTime.length == getItemSlots()) ? totalTime : new int[getItemSlots()];

        this.prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
        if (tag.contains("prevItems")) {
            ContainerHelper.loadAllItems(tag.getCompound("prevItems"), this.prevItems);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("litState", this.litState.getBooleanState());
        tag.putInt("LitTime", this.litTime);
        tag.putBoolean("prevLitState", this.prevLitState.getBooleanState());
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
        CompoundTag prevItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(prevItemsTag, prevItems);
        tag.put("prevItems", prevItemsTag);
    }

    @Override
    public boolean isLit() { return this.litState.equals(FireState.LIT); }

    @Override
    public boolean isHaunt() { return false; }

    @Override
    public void litTick() {
        litTime -= getFuelConsumptionRate();
        if (litTime <= 0) {
            litState = FireState.NONE;
            litTime = 0;
            if (prevLitState.equals(FireState.LIT)) burnOneFuel();
        }
        prevLitState = litState;
    }

    public void burnOneFuel() {
        ItemStack stack = fuels.extractItem(true);
        int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        if (stack.isEmpty() || burnTime <= 0) return;

        stack = fuels.extractItem(false);
        this.litState = FireState.LIT;
        this.litTime += burnTime;

        if (stack.hasCraftingRemainingItem()) {
            ItemStack toInsert = stack.getCraftingRemainingItem();
            if (fuels.insertItem(toInsert, true).isEmpty()) fuels.insertItem(toInsert, false);
            else Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }

        float chance = DataMapHook.getFireAshDropChance(stack);
        if (getLevel().getRandom().nextFloat() < chance) {
            ItemStack toInsert = new ItemStack(AllBlockItem.FIRE_ASH.get());
            if (fuels.insertItem(toInsert, true).isEmpty()) fuels.insertItem(toInsert, false);
            else Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
    }

    @Override
    public void updateCookingTime() {
        int cookingMultiplier = getCookingMultiplier();
        for (int i = 0; i < getItemSlots(); i++) {
            ItemStack currentStack = items.getStackInSlot(i);
            ItemStack prevStack = prevItems.get(i);

            if (!isLit()) cookingProgress[i] = Math.max(0, cookingProgress[i] - coolRate);

            if (ItemStack.matches(currentStack, prevStack)) {
                if (isLit()) cookingProgress[i] += cookingMultiplier;
            } else if (ItemStack.isSameItemSameTags(currentStack, prevStack)) {
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, currentStack, processMultiplier);
            } else {
                cookingProgress[i] = 0;
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, currentStack, processMultiplier);
            }
            prevItems.set(i, currentStack.copy());
        }
    }

    @Override
    public void smeltItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            if (cookingTotalTime[i] != 0 && cookingProgress[i] >= cookingTotalTime[i]) {
                ItemStack ingredient = items.getStackInSlot(i);
                Optional<SmeltingRecipe> recipeOptional = RecipeUtil.getCookRecipe(level, RecipeType.SMELTING, ingredient);

                if (recipeOptional.isPresent()) {
                    SmeltingRecipe recipe = recipeOptional.get();
                    ItemStack result = recipe.assemble(new SimpleContainer(ingredient), level.registryAccess());
                    if (!result.isEmpty()) {
                        result.setCount(ingredient.getCount());
                        items.setStackInSlot(i, result);
                        if (this.getPlacer() != null && level instanceof ServerLevel serverLevel) {
                            if (serverLevel.getPlayerByUUID(this.getPlacer()) instanceof ServerPlayer serverPlayer) {
                                net.minecraft.advancements.CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, recipe.getId(), Collections.singletonList(result));
                            }
                        }
                    }
                }
            }
        }
    }

    public void kindle() {
        if (this.isLit()) return;
        burnOneFuel();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CopperStoveBlockEntity entity) {
        if (level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), net.minecraft.core.Direction.DOWN)) {
            entity.dropItem();
        }

        entity.litTick();
        entity.updateCookingTime();
        entity.smeltItem();
        entity.smolderBlock();

        if (state.getValue(CopperStoveBlock.LIT) != entity.isLit()) {
            entity.updateBlockState(entity.getBlockState().setValue(CopperStoveBlock.LIT, entity.isLit()));
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CopperStoveBlockEntity entity) {
        if (!state.getValue(CopperStoveBlock.LIT)) return;

        RandomSource randomsource = level.random;
        if (randomsource.nextFloat() < 0.11F) {
            for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
                level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                        pos.getX() + 0.5 + randomsource.nextDouble() / 3.0 * (randomsource.nextBoolean() ? 1 : -1),
                        pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                        pos.getZ() + 0.5 + randomsource.nextDouble() / 3.0 * (randomsource.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }

        int facingValue = state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue();
        for (int j = 0; j < entity.items.getSlots(); ++j) {
            if (!entity.items.getStackInSlot(j).isEmpty() && randomsource.nextFloat() < 0.2F) {
                net.minecraft.core.Direction direction = net.minecraft.core.Direction.from2DDataValue(Math.floorMod(j + facingValue, 4));
                float offset = 0.3125F;
                double x = pos.getX() + 0.5D - direction.getStepX() * offset + direction.getClockWise().getStepX() * offset;
                double y = pos.getY() + 1.0D;
                double z = pos.getZ() + 0.5D - direction.getStepZ() * offset + direction.getClockWise().getStepZ() * offset;
                for (int k = 0; k < 4; ++k) {
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void updateBlockEntity() {
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void updateBlockState(BlockState newState) {
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }
}
