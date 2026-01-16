package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.capability.QueueItemStackHandler;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockSmolderingRecipeInput;
import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private Component name;
    public final QueueItemStackHandler fuels;
    public final ItemStackHandler items;

    public int tier;
    public int smolderingProgress[];
    public int smolderingTotalTime[];
    public BlockState blockState;

    private final RecipeManager.CachedCheck<BlockSmolderingRecipeInput, BlockSmolderingRecipe> blockSmolderQuickCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> smeltingQuickCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, HauntingRecipe> hauntingQuickCheck;

    @Nullable
    protected UUID placer;

    public AbstractStoveBlockEntity(BlockEntityType<? extends AbstractStoveBlockEntity> type, BlockPos pos, BlockState blockState, int fuelCapacity, Predicate<ItemStack> isFuelValid, int itemCapacity, int tier) {
        super(type, pos, blockState);
        this.fuels = new QueueItemStackHandler(fuelCapacity, 1) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return isFuelValid.test(stack);
            }

            @Override
            protected void onContentsChanged() {
                updateBlockEntity();
            }
        };
        this.items = new ItemStackHandler(itemCapacity) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                updateBlockEntity();
            }
        };
        this.tier = tier;
        this.smolderingProgress = new int[2];
        this.smolderingTotalTime = new int[2];
        this.blockState = Blocks.AIR.defaultBlockState();
        this.blockSmolderQuickCheck = RecipeManager.createCheck(AllRecipe.BLOCK_SMOLDERING_RECIPE.get());
        this.smeltingQuickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        this.hauntingQuickCheck = RecipeManager.createCheck(AllRecipe.HAUNTING.get());
    }

    public QueueItemStackHandler getFuels() {
        return fuels;
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public int getFuelSlots() {
        return getFuels().getSlots();
    }

    public int getItemSlots() {
        return getItems().getSlots();
    }

    public ItemStack getStackInFuels(int i) {
        return getFuels().getStackInSlot(i);
    }

    public ItemStack getStackInItems(int i) {
        return getItems().getStackInSlot(i);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("CustomName", 8)) {
            this.name = parseCustomNameSafe(tag.getString("CustomName"), registries);
        }

        if (tag.contains("items")) {
            items.deserializeNBT(registries, tag.getCompound("items"));
        }
        if (tag.contains("fuels")) {
            fuels.deserializeNBT(registries, tag.getCompound("fuels"));
        }

        // 3. Primitives: Check existence to preserve constructor defaults if tag is missing
        if (tag.contains("Tier")) {
            this.tier = tag.getInt("Tier");
        }

        // 4. Arrays: CRITICAL FIX
        // getIntArray returns int[0] if missing. We must ensure size is 2 to avoid crashes.
        int[] progress = tag.getIntArray("SmolderingProgress");
        this.smolderingProgress = (progress.length == 2) ? progress : new int[2];

        int[] totalTime = tag.getIntArray("SmolderingTotalTime");
        this.smolderingTotalTime = (totalTime.length == 2) ? totalTime : new int[2];

        // 5. BlockState Loading
        if (tag.contains("StoredBlockState")) {
            try {
                this.blockState = NbtUtils.readBlockState(
                        registries.lookupOrThrow(Registries.BLOCK),
                        tag.getCompound("StoredBlockState")
                );
            } catch (Exception e) {
                // Fallback if registry lookup fails or state is corrupt
                this.blockState = Blocks.AIR.defaultBlockState();
            }
        } else {
            this.blockState = Blocks.AIR.defaultBlockState();
        }

        if (tag.hasUUID("placer")) {
            this.placer = tag.getUUID("placer");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name, registries));
        }
        tag.put("items", items.serializeNBT(registries));
        tag.put("fuels", fuels.serializeNBT(registries));
        tag.putInt("Tier", this.tier);
        tag.putIntArray("SmolderingProgress", this.smolderingProgress);
        tag.putIntArray("SmolderingTotalTime", this.smolderingTotalTime);
        if (this.blockState != null) {
            tag.put("StoredBlockState", NbtUtils.writeBlockState(this.blockState));
        }

        if (this.placer != null) {
            tag.putUUID("placer", this.placer);
        }
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
        this.name = (Component) componentInput.get(DataComponents.CUSTOM_NAME);
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

    public abstract void updateBlockState(BlockState newState);

    public abstract boolean isLit();

    public abstract boolean isHaunt();

    public abstract void litTick();

    public abstract void updateCookingTime();

    public abstract void smeltItem();

    @Nullable
    public UUID getPlacer() {
        return placer;
    }

    public void setPlacer(@Nullable UUID placer) {
        this.placer = placer;
    }

    public void dropFuel() {
        for (int i = 0; i < fuels.getSlots(); i++) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, this.fuels.getStackInSlot(i)));
            fuels.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void dropItem() {
        for (int i = 0; i < items.getSlots(); i++) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.8, this.getBlockPos().getZ() + 0.5, this.items.getStackInSlot(i)));
            items.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void dropContent() {
        dropFuel();
        dropItem();
    }

    public void smolderBlock() {
        if (this.level == null || this.level.isClientSide) return;
        BlockState currentBlockState = getLevel().getBlockState(getBlockPos().above());
        if (currentBlockState == null || currentBlockState.isAir()) {
            this.smolderingProgress[0] = 0;
            this.smolderingProgress[1] = 0;
            return;
        }
        if (currentBlockState != this.blockState) {
            this.smolderingProgress[0] = 0;
            this.smolderingProgress[1] = 0;
        }
        this.blockState = currentBlockState;

        for (int i = 0; i < 2; i++) {
            boolean active = (i == 0) ? isLit() : isHaunt();
            if (!active) {
                if (this.smolderingProgress[i] > 0) {
                    this.smolderingProgress[i] = Math.max(0, this.smolderingProgress[i] - 2);
                }
                continue;
            }

            BlockSmolderingRecipeInput input = new BlockSmolderingRecipeInput(this.blockState, i + 1, false);
            Optional<RecipeHolder<BlockSmolderingRecipe>> recipeOptional =
                    blockSmolderQuickCheck.getRecipeFor(input, getLevel());

            if (recipeOptional.isPresent()) {
                BlockSmolderingRecipe recipe = recipeOptional.get().value();
                if (this.smolderingTotalTime[i] != recipe.getTime()) {
                    this.smolderingTotalTime[i] = recipe.getTime();
                }
                this.smolderingProgress[i]++;
                if (this.smolderingProgress[i] >= this.smolderingTotalTime[i]) {
                    getLevel().destroyBlock(getBlockPos().above(), false);
                    getLevel().setBlock(getBlockPos().above(), recipe.getResultBlock(), Block.UPDATE_ALL);
                    getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), recipe.getResultBlock(), Block.UPDATE_ALL);
                    for (ItemStack stack : recipe.getItemOutputs())
                        if (!stack.isEmpty())
                            Containers.dropItemStack(level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1f, getBlockPos().getZ() + 0.5f, stack);
                    if (this.placer != null && level instanceof ServerLevel serverLevel) {
                        if (serverLevel.getPlayerByUUID(this.placer) instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, recipeOptional.get().id(), recipe.getItemOutputs());
                        }
                    }
                    this.smolderingProgress[i] = 0;
                    break;
                }
            } else {
                ItemStack stack = new ItemStack(this.blockState.getBlock().asItem());
                Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> fallbackOptRecipe;

                if (i == 0) {
                    fallbackOptRecipe = smeltingQuickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level);
                } else {
                    fallbackOptRecipe = hauntingQuickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level);
                }

                if (fallbackOptRecipe.isPresent()) {
                    AbstractCookingRecipe recipe = fallbackOptRecipe.get().value();
                    int fallbackTime = recipe.getCookingTime();
                    if (this.smolderingTotalTime[i] != fallbackTime) {
                        this.smolderingTotalTime[i] = fallbackTime;
                    }
                    this.smolderingProgress[i]++;
                    if (this.smolderingProgress[i] >= this.smolderingTotalTime[i]) {
                        ItemStack fallbackResult = recipe.getResultItem(this.level.registryAccess());
                        if (fallbackResult.getItem() instanceof BlockItem blockItem) {
                            this.getLevel().destroyBlock(getBlockPos().above(), false);
                            this.getLevel().setBlock(getBlockPos().above(), blockItem.getBlock().defaultBlockState(), Block.UPDATE_ALL);
                        } else {
                            Containers.dropItemStack(level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1f, getBlockPos().getZ() + 0.5f, fallbackResult.copy());
                            getLevel().destroyBlock(getBlockPos().above(), false);
                            getLevel().setBlock(getBlockPos().above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        }
                        if (this.placer != null && level instanceof ServerLevel serverLevel) {
                            if (serverLevel.getPlayerByUUID(this.placer) instanceof ServerPlayer serverPlayer) {
                                CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, fallbackOptRecipe.get().id(), Collections.singletonList(fallbackResult));
                            }
                        }
                        this.smolderingProgress[i] = 0;
                        this.updateBlockEntity();
                        break;
                    }
                } else {
                    if (this.smolderingProgress[i] > 0) {
                        this.smolderingProgress[i] = Math.max(0, this.smolderingProgress[i] - 2);
                    }
                }
            }
        }
    }
}
