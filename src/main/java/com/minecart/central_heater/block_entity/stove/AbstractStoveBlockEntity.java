package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.capability.QueueItemStackHandler;
import com.minecart.central_heater.heat.api.HeatBlockEntityBehavior;
import com.minecart.central_heater.heat.api.HeatApi;
import com.minecart.central_heater.heat.api.HeatEmission;
import com.minecart.central_heater.heat.api.HeatSink;
import com.minecart.central_heater.heat.api.HeatType;
import com.minecart.central_heater.heat.context.HeatNodeAccess;
import com.minecart.central_heater.heat.context.HeatNodeContext;
import com.minecart.central_heater.heat.storage.HeatNode;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockSmolderingRecipeInput;
import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Nameable, HeatBlockEntityBehavior {
    @Nullable
    private Component name;
    public final QueueItemStackHandler fuels;
    public final ItemStackHandler items;

    public int tier;
    public int[] smolderingProgress;
    public int[] smolderingTotalTime;
    public BlockState blockState;

    public int litTime;
    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> prevItems;

    private final RecipeManager.CachedCheck<BlockSmolderingRecipeInput, BlockSmolderingRecipe> blockSmolderQuickCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> smeltingQuickCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, HauntingRecipe> hauntingQuickCheck;

    @Nullable
    protected UUID placer;

    public AbstractStoveBlockEntity(BlockEntityType<? extends AbstractStoveBlockEntity> type, BlockPos pos, BlockState blockState, int fuelCapacity, Predicate<ItemStack> isFuelValid, int itemCapacity, int tier) {
        super(type, pos, blockState);
        this.fuels = new QueueItemStackHandler(fuelCapacity, 1) {
            @Override
            public boolean isItemValid(ItemStack stack) { return isFuelValid.test(stack); }
            @Override
            protected void onContentsChanged() { updateBlockEntity(); }
        };
        this.items = new ItemStackHandler(itemCapacity) {
            @Override
            public int getSlotLimit(int slot) { return 1; }
            @Override
            protected void onContentsChanged(int slot) { updateBlockEntity(); }
        };
        this.tier = tier;
        this.smolderingProgress = new int[2];
        this.smolderingTotalTime = new int[2];
        this.blockState = Blocks.AIR.defaultBlockState();

        this.litTime = 0;
        this.cookingProgress = new int[itemCapacity];
        this.cookingTotalTime = new int[itemCapacity];
        this.prevItems = NonNullList.withSize(itemCapacity, ItemStack.EMPTY);

        this.blockSmolderQuickCheck = RecipeManager.createCheck(AllRecipe.BLOCK_SMOLDERING_RECIPE.get());
        this.smeltingQuickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        this.hauntingQuickCheck = RecipeManager.createCheck(AllRecipe.HAUNTING.get());
    }

    // ... (Keep existing getter/setter methods like getFuels, getPlacer, dropItem) ...
    public QueueItemStackHandler getFuels() { return fuels; }
    public ItemStackHandler getItems() { return items; }
    public int getFuelSlots() { return getFuels().getSlots(); }
    public int getItemSlots() { return getItems().getSlots(); }
    public ItemStack getStackInFuels(int i) { return getFuels().getStackInSlot(i); }
    public ItemStack getStackInItems(int i) { return getItems().getStackInSlot(i); }
    public Component getName() { return this.name != null ? this.name : Component.empty(); }
    public Component getDisplayName() { return this.getName(); }
    @Nullable public Component getCustomName() { return this.name; }
    @Nullable public UUID getPlacer() { return placer; }
    public void setPlacer(@Nullable UUID placer) { this.placer = placer; }

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
    public void removeComponentsFromTag(CompoundTag tag) { tag.remove("CustomName"); }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CustomName", 8)) this.name = parseCustomNameSafe(tag.getString("CustomName"), registries);
        if (tag.contains("items")) items.deserializeNBT(registries, tag.getCompound("items"));
        if (tag.contains("fuels")) fuels.deserializeNBT(registries, tag.getCompound("fuels"));
        if (tag.contains("Tier")) this.tier = tag.getInt("Tier");

        int[] sProgress = !tag.contains("SmolderingProgress") ? new int[0] : tag.getIntArray("SmolderingProgress");
        this.smolderingProgress = (sProgress.length == 2) ? sProgress : new int[2];
        int[] sTotalTime = !tag.contains("SmolderingTotalTime") ? new int[0] : tag.getIntArray("SmolderingTotalTime");
        this.smolderingTotalTime = (sTotalTime.length == 2) ? sTotalTime : new int[2];

        if (tag.contains("StoredBlockState")) {
            try { this.blockState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("StoredBlockState"));
            } catch (Exception e) { this.blockState = Blocks.AIR.defaultBlockState(); }
        } else { this.blockState = Blocks.AIR.defaultBlockState(); }
        if (tag.hasUUID("placer")) this.placer = tag.getUUID("placer");

        // Unified Loads
        this.litTime = !tag.contains("LitTime") ? 0 : tag.getInt("LitTime");
        int[] progress = !tag.contains("cookingProgress") ? new int[0] : tag.getIntArray("cookingProgress");
        this.cookingProgress = (progress.length == getItemSlots()) ? progress : new int[getItemSlots()];
        int[] totalTime = !tag.contains("cookingTotalTime") ? new int[0] : tag.getIntArray("cookingTotalTime");
        this.cookingTotalTime = (totalTime.length == getItemSlots()) ? totalTime : new int[getItemSlots()];
        this.prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
        if (tag.contains("prevItems")) ContainerHelper.loadAllItems(tag.getCompound("prevItems"), this.prevItems, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.name != null) tag.putString("CustomName", Component.Serializer.toJson(this.name, registries));
        tag.put("items", items.serializeNBT(registries));
        tag.put("fuels", fuels.serializeNBT(registries));
        tag.putInt("Tier", this.tier);
        tag.putIntArray("SmolderingProgress", this.smolderingProgress);
        tag.putIntArray("SmolderingTotalTime", this.smolderingTotalTime);
        if (this.blockState != null) tag.put("StoredBlockState", NbtUtils.writeBlockState(this.blockState));
        if (this.placer != null) tag.putUUID("placer", this.placer);

        // Unified Saves
        tag.putInt("LitTime", this.litTime);
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
        CompoundTag prevItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(prevItemsTag, prevItems, registries);
        tag.put("prevItems", prevItemsTag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void updateBlockEntity(){
        setChanged(getLevel(), this.getBlockPos(), getBlockState());
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
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

    // --- Abstract Methods & Common Logic ---
    public abstract boolean isLit();
    public abstract boolean isHaunt();
    public abstract void litTick();
    public abstract void updateCookingTime();
    public abstract void smeltItem();
    public abstract void syncLitState(BlockState state);
    public abstract void burnOneFuel();

    @Override
    public HeatNode createHeatNode() {
        return new HeatNode(getBlockPos(), isLit() || isHaunt() ? getHeatTarget() / 2 : 0, 900 + tier * 240, isHaunt() ? HeatType.SOUL : HeatType.NORMAL);
    }

    @Override
    public HeatEmission getEmission(HeatNodeContext ctx) {
        if (isHaunt()) {
            return new HeatEmission(20 + tier * 5, 260 + tier * 45, 460 + tier * 60, HeatType.SOUL);
        }
        if (isLit()) {
            return new HeatEmission(14 + tier * 4, getHeatTarget(), getHeatTarget() + 140, HeatType.NORMAL);
        }
        return HeatEmission.NONE;
    }

    @Override
    public HeatSink getSink(HeatNodeContext ctx) {
        return isLit() || isHaunt() ? HeatSink.NONE : new HeatSink(3 + tier, 0, 3 + tier, false);
    }

    @Override
    public void tickHeatNode(HeatNodeContext ctx, HeatNodeAccess heat) {
        if (!isLit() && !isHaunt() && heat.getHeat() > 0) {
            heat.setHeat(Math.max(0, heat.getHeat() - (3 + tier)));
        }
    }

    protected int getHeatTarget() {
        return 190 + tier * 35;
    }

    public void kindle() {
        if(this.isLit() || this.isHaunt()) return;
        burnOneFuel();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity entity) {
        if(level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN))
            entity.dropItem();

        entity.litTick();
        entity.updateCookingTime();
        entity.smeltItem();
        entity.smolderBlock();

        entity.syncLitState(state);
        if (level instanceof ServerLevel serverLevel && (entity.isLit() || entity.isHaunt() || entity.litTime > 0)) {
            HeatApi.touch(serverLevel, pos);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity entity){
        if (!entity.isLit() && !entity.isHaunt()) return;

        RandomSource randomsource = level.random;
        if (randomsource.nextFloat() < 0.11F) {
            for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
                level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,true,
                        (double)pos.getX() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                        (double)pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                        (double)pos.getZ() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                        0.0, 0.07, 0.0);
            }
        }

        int facingValue = state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue();
        for (int j = 0; j < entity.items.getSlots(); ++j) {
            if (!entity.items.getStackInSlot(j).isEmpty() && randomsource.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + facingValue, 4));
                float offset = 0.3125F;
                double x = (double) pos.getX() + 0.5D - (double) ((float) direction.getStepX() * offset) + (double) ((float) direction.getClockWise().getStepX() * offset);
                double y = (double) pos.getY() + 1.0D;
                double z = (double) pos.getZ() + 0.5D - (double) ((float) direction.getStepZ() * offset) + (double) ((float) direction.getClockWise().getStepZ() * offset);
                for (int k = 0; k < 4; ++k) level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
            }
        }
    }

    // Keep your existing smolderBlock() implementation exactly as it is here
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
                if (this.smolderingProgress[i] > 0) this.smolderingProgress[i] = Math.max(0, this.smolderingProgress[i] - 2);
                continue;
            }

            BlockSmolderingRecipeInput input = new BlockSmolderingRecipeInput(this.blockState, i + 1, false);
            Optional<RecipeHolder<BlockSmolderingRecipe>> recipeOptional = blockSmolderQuickCheck.getRecipeFor(input, getLevel());

            if (recipeOptional.isPresent()) {
                BlockSmolderingRecipe recipe = recipeOptional.get().value();
                if (this.smolderingTotalTime[i] != recipe.getTime()) this.smolderingTotalTime[i] = recipe.getTime();
                this.smolderingProgress[i]++;
                if (this.smolderingProgress[i] >= this.smolderingTotalTime[i]) {
                    getLevel().destroyBlock(getBlockPos().above(), false);
                    getLevel().setBlock(getBlockPos().above(), recipe.getResultBlock(), Block.UPDATE_ALL);
                    getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), recipe.getResultBlock(), Block.UPDATE_ALL);
                    for (ItemStack stack : recipe.getItemOutputs())
                        if (!stack.isEmpty()) Containers.dropItemStack(level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1f, getBlockPos().getZ() + 0.5f, stack);
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
                if (i == 0) fallbackOptRecipe = smeltingQuickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level);
                else fallbackOptRecipe = hauntingQuickCheck.getRecipeFor(new SingleRecipeInput(stack), this.level);

                if (fallbackOptRecipe.isPresent()) {
                    AbstractCookingRecipe recipe = fallbackOptRecipe.get().value();
                    int fallbackTime = recipe.getCookingTime();
                    if (this.smolderingTotalTime[i] != fallbackTime) this.smolderingTotalTime[i] = fallbackTime;
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
                    if (this.smolderingProgress[i] > 0) this.smolderingProgress[i] = Math.max(0, this.smolderingProgress[i] - 2);
                }
            }
        }
    }
}
