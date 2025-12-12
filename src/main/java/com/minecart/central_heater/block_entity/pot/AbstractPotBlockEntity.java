package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.block.*;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipeInput;
import com.minecart.central_heater.util.NetherFireState;
import com.minecart.central_heater.capability.QueueItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.IntStream;

public abstract class AbstractPotBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private Component name;

    public final int tier;

    protected final QueueItemStackHandler container;
    public int[][] progress;

    public final int fluidTankSize;
    protected final FluidTank fluidTank;

    public static final BlockCapability<IFluidHandler, Direction> fluidCap = BlockCapability.createSided(Central_heater.modLoc("pot_tank"), IFluidHandler.class);

    NonNullList<ItemStack> prevContainer;
    public boolean hasRecipe = false;

    protected AbstractPotBlockEntity(BlockEntityType<? extends AbstractPotBlockEntity> type, BlockPos pos, BlockState blockState, int tier) {
        super(type, pos, blockState);
        this.container = new QueueItemStackHandler(4, 1){
            @Override
            protected void onContentsChanged() {
                updateBlockEntity();
            }
        };

        this.prevContainer = NonNullList.withSize(4, ItemStack.EMPTY);
        this.progress = new int[3][4];

        this.fluidTankSize = 1000;
        this.fluidTank = new FluidTank(this.fluidTankSize){
            @Override
            protected void onContentsChanged() {
                updateBlockEntity();
            }
        };

        this.tier = tier;
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CustomName", 8)) {
            this.name = parseCustomNameSafe(tag.getString("CustomName"), registries);
        }
        container.deserializeNBT(registries, tag.getCompound("container"));
        fluidTank.readFromNBT(registries, tag.getCompound("fluidTank"));
        progress[0] = tag.getIntArray("brewingProgress");
        progress[0] = tag.getIntArray("cookingProgress");
        progress[0] = tag.getIntArray("seethingProgress");
        ContainerHelper.loadAllItems(tag.getCompound("prevContainer"), prevContainer, registries);
        hasRecipe = tag.getBoolean("hasRecipe");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name, registries));
        }
        tag.put("container", container.serializeNBT(registries));
        tag.put("fluidTank", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.putIntArray("brewingProgress", progress[0]);
        tag.putIntArray("cookingProgress", progress[1]);
        tag.putIntArray("seethingProgress", progress[2]);
        ContainerHelper.saveAllItems(tag.getCompound("prevContainer"), prevContainer, registries);
        tag.putBoolean("hasRecipe", hasRecipe);
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

    public QueueItemStackHandler getContainer(){
        return this.container;
    }

    public IFluidHandler getFluidTank(){
        return this.fluidTank;
    }

    public int getItemSlots(){
        return getItems().size();
    }

    public NonNullList<ItemStack> getItems(){
        return getContainer().get();
    }

    public ItemStack getStackInSlot(int i){
        return getContainer().getStackInSlot(i);
    }


    public void updateBlockEntity() {
        setChanged(getLevel(), this.getBlockPos(), getBlockState());
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }

    public void drop(){
        for(ItemStack stack : getItems())
            getLevel().addFreshEntity(new ItemEntity(getLevel(), getBlockPos().getX()+0.5, getBlockPos().getY()+0.5, getBlockPos().getZ()+0.5, stack));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity entity){
        RecipeManager recipeManager = level.getRecipeManager();

        if(true){
            for(int i=0;i<entity.getItemSlots();i++){
                ItemStack stack = entity.getStackInSlot(i);
                entity.progress[0][i] = Math.max(entity.progress[0][i]-2, 0);
                entity.progress[1][i] = Math.max(entity.progress[1][i]-2, 0);
                entity.progress[2][i] = Math.max(entity.progress[2][i]-2, 0);
                if(!stack.isEmpty()) {
                    entity.progress[entity.heatLevel().getState()][i] += 3;
                }
                if(stack.isEmpty() || !ItemStack.isSameItemSameComponents(entity.prevContainer.get(i), stack)) {
                    entity.progress[0][i] = 0;
                    entity.progress[1][i] = 0;
                    entity.progress[2][i] = 0;
                }
                entity.prevContainer.set(i, entity.getStackInSlot(i).copy());
            }
        }

        NonNullList<ItemStack> stack = entity.getItems();
        int slots = stack.size();

        entity.hasRecipe = false;

        while (true) {
            boolean flag = true;
            for (int i = 0; i < (1 << slots); i++) {
                NonNullList<ItemStack> substack = NonNullList.create();
                int minHeat = 114514;
                boolean flag1 = false;
                for (int j = 0; j < slots; j++) {
                    if ((i & (1 << j)) != 0) {
                        if(stack.get(j).isEmpty()) {
                            flag1 = true;
                            break;
                        }
                        substack.add(stack.get(j));
                        minHeat = Math.min(minHeat, entity.progress[entity.heatLevel().getState()][j]);
                    }
                }
                if(flag1)
                    continue;
                SmolderingRecipeInput input = new SmolderingRecipeInput(substack, entity.getFluidTank().getFluidInTank(0), entity.tier, entity.heatLevel().getState());
                Optional<RecipeHolder<SmolderingRecipe>> recipeHolder = recipeManager.getRecipeFor(AllRecipe.SMOLDERING.get(),
                        input, level);
                if (recipeHolder.isEmpty())
                    continue;
                SmolderingRecipe recipe = recipeHolder.get().value();
                FluidStack fluidIngredient = recipe.getFluidIngredient(input, level.registryAccess());
                int time = recipe.getTime(input, level.registryAccess());
                NonNullList<ItemStack> results = recipe.assembleResults(input, level.registryAccess());
                FluidStack fluidResult = recipe.assembleFluidResult(input, level.registryAccess());
                if (entity.getContainer().getNonEmptyItems() - substack.size() + results.size() > entity.getItemSlots())
                    continue;
                if (!entity.getFluidTank().getFluidInTank(0).isEmpty() && !fluidResult.isEmpty() &&
                        !FluidStack.isSameFluidSameComponents(entity.getFluidTank().getFluidInTank(0), fluidResult)
                && entity.getFluidTank().getFluidInTank(0).getAmount() > fluidIngredient.getAmount())
                    continue;
                if (entity.getFluidTank().getFluidInTank(0).getAmount() + fluidResult.getAmount() - fluidIngredient.getAmount() > entity.getFluidTank().getTankCapacity(0))
                    continue;
                entity.hasRecipe = true;
                if(minHeat < time)
                    continue;
                flag = false;
                for (ItemStack stack1 : substack) {
                    stack1.setCount(0);
                }
                entity.fluidTank.drain(fluidIngredient, IFluidHandler.FluidAction.EXECUTE);
                for(ItemStack result : results)
                    entity.getContainer().insertItem(result.copy(), false);
                entity.getFluidTank().fill(fluidResult.copy(), IFluidHandler.FluidAction.EXECUTE);
            }
            if (flag)
                break;
        }

        entity.updateBlockState(state.setValue(PotBlock.LEVEL, entity.calculateLight()));
    }

    private NetherFireState heatLevel() {
        BlockState state = getLevel().getBlockState(getBlockPos().below());
        if(state.getBlock() instanceof GoldenStoveBlock block){
            return state.getValue(GoldenStoveBlock.LIT_SOUL);
        }else if(state.getBlock() instanceof StoneStoveBlock || state.getBlock() instanceof BrickStoveBlock){
            if(state.getValue(BlockStateProperties.LIT))
                return NetherFireState.BURN;
        }
        return NetherFireState.NONE;
    }

    public int calculateLight(){
        FluidState fluidState = getFluidTank().getFluidInTank(0).getFluid().defaultFluidState();
        int fluidLight = fluidState.createLegacyBlock().getLightEmission(level, getBlockPos());
        int itemLight = -1;
        for(ItemStack stack : getItems()){
            if(stack.getItem() instanceof BlockItem blockItem){
                itemLight = Math.max(itemLight, blockItem.getBlock().defaultBlockState().getLightEmission(level, getBlockPos()));
            }
        }
        return Math.max(fluidLight, itemLight);
    }


    @OnlyIn(Dist.CLIENT)
    public FluidStack clientFluidType = FluidStack.EMPTY;
    @OnlyIn(Dist.CLIENT)
    public float clientFluid = 0f;
    @OnlyIn(Dist.CLIENT)
    public float prevClientFluid = 0f;

    @OnlyIn(Dist.CLIENT)
    public float clientSpin = 0f;
    @OnlyIn(Dist.CLIENT)
    public float prevClientSpin = 0f;
    @OnlyIn(Dist.CLIENT)
    public float spinVelocity = 0f;
    @OnlyIn(Dist.CLIENT)
    public float prevSpinVelocity = 0f;

    @OnlyIn(Dist.CLIENT)
    public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity entity){
        FluidStack serverFluid = entity.getFluidTank().getFluidInTank(0);
        float serverAmount = serverFluid.getAmount();
        entity.prevClientFluid = entity.clientFluid;
        float clientAmount = entity.clientFluid;
        clientAmount = clientAmount * 0.8f + serverAmount * 0.2f;
        if(Math.abs(serverAmount - clientAmount) <= 1f)
            clientAmount = serverAmount;
        if(!serverFluid.isEmpty())
            entity.clientFluidType = serverFluid.copy();
        entity.clientFluid = clientAmount;


        entity.prevClientSpin = entity.clientSpin;
        entity.prevSpinVelocity = entity.spinVelocity;

        float maxSpeed = 2f;
        float acceleration = 0.5f;
        float brakeFactor = 0.15f;
        float stopThreshold = 3f;

        if (entity.hasRecipe) {
            if (entity.spinVelocity < maxSpeed) {
                entity.spinVelocity = Math.min(entity.spinVelocity + acceleration, maxSpeed);
            }
        } else {
            if (entity.spinVelocity == 0) {
                entity.clientSpin = 0;
                entity.prevClientSpin = 0;
                return;
            }
            if (entity.clientSpin > 300f) {
                float distanceRemaining = 360f - entity.clientSpin;
                float arrivalSpeed = distanceRemaining * brakeFactor;
                entity.spinVelocity = Math.min(entity.spinVelocity, arrivalSpeed);

                if (distanceRemaining < stopThreshold) {
                    entity.clientSpin = 0f;
                    entity.prevClientSpin = 0f;
                    entity.spinVelocity = 0f;
                    return;
                }
            } else {
                if (entity.spinVelocity < maxSpeed) {
                    entity.spinVelocity = Math.min(entity.spinVelocity + acceleration, maxSpeed);
                }
            }
        }

        entity.clientSpin += entity.spinVelocity;

        if (entity.clientSpin >= 360f) {
            entity.clientSpin -= 360f;
            entity.prevClientSpin -= 360f;
        }
    }
}
