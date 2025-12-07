package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.block.*;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipeInput;
import com.minecart.central_heater.util.NetherFireState;
import com.minecart.central_heater.capability.StackItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.IntStream;

public abstract class AbstractPotBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public final int tier;

    public final int containerSize;
    public final int[] containerSlot;
    protected final StackItemHandler container;
    public int[][] progress;
    NonNullList<ItemStack> prevContainer;

    public final int fluidTankSize;
    protected final FluidTank fluidTank;

    @OnlyIn(Dist.CLIENT)
    public FluidStack clientFluidType = FluidStack.EMPTY;
    @OnlyIn(Dist.CLIENT)
    public float clientFluid = 0f;
    @OnlyIn(Dist.CLIENT)
    public float prevClientFluid = 0;

    public static final BlockCapability<IFluidHandler, Direction> fluidCap = BlockCapability.createSided(Central_heater.modLoc("pot_tank"), IFluidHandler.class);

    protected AbstractPotBlockEntity(BlockEntityType<? extends AbstractPotBlockEntity> type, BlockPos pos, BlockState blockState, int tier) {
        super(type, pos, blockState);
        this.containerSize = 4;
        this.containerSlot = IntStream.range(0, this.containerSize).toArray();
        this.container = new StackItemHandler(this.containerSize, 1){
            @Override
            protected void onContentsChanged() {
                updateBlockEntity();
            }
        };

        this.prevContainer = NonNullList.withSize(this.containerSize, ItemStack.EMPTY);
        this.progress = new int[3][this.containerSize];

        this.fluidTankSize = 1000;
        this.fluidTank = new FluidTank(this.fluidTankSize){
            @Override
            protected void onContentsChanged() {
                updateBlockEntity();
            }
        };

        this.tier = tier;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        container.deserializeNBT(registries, tag.getCompound("container"));
        fluidTank.readFromNBT(registries, tag.getCompound("fluidTank"));
        progress[0] = tag.getIntArray("brewingProgress");
        progress[0] = tag.getIntArray("cookingProgress");
        progress[0] = tag.getIntArray("seethingProgress");
        ContainerHelper.loadAllItems(tag.getCompound("prevContainer"), prevContainer, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("container", container.serializeNBT(registries));
        tag.put("fluidTank", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.putIntArray("brewingProgress", progress[0]);
        tag.putIntArray("cookingProgress", progress[1]);
        tag.putIntArray("seethingProgress", progress[2]);
        ContainerHelper.saveAllItems(tag.getCompound("prevContainer"), prevContainer, registries);
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

    public StackItemHandler getContainer(){
        return this.container;
    }

    public IFluidHandler getFluidTank(){
        return this.fluidTank;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : container.get())
            if(!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return container.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return container.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return container.extractItem(slot, Item.ABSOLUTE_MAX_STACK_SIZE, false);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        container.setStackInSlot(slot, stack);
        updateBlockEntity();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return containerSlot;
    }

    @Override
    public int getMaxStackSize() {
        return container.getSlotLimit();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return container.get();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        container.set(items);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return containerSize;
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
        for(ItemStack stack : this.getItems())
            getLevel().addFreshEntity(new ItemEntity(getLevel(), getBlockPos().getX()+0.5, getBlockPos().getY()+0.5, getBlockPos().getZ()+0.5, stack));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity entity){
        RecipeManager recipeManager = level.getRecipeManager();

        if(true){
            for(int i=0;i<entity.containerSize;i++){
                ItemStack stack = entity.getItem(i);
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
                entity.prevContainer.set(i, entity.getContainer().getStackInSlot(i).copy());
            }
        }

        NonNullList<ItemStack> stack = entity.getItems();
        int slots = stack.size();

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
                if(minHeat < time)
                    continue;
                if (entity.getContainer().getNonEmptyItems() + substack.size() - results.size() > entity.getContainer().getSlots())
                    continue;
                if (!entity.getFluidTank().getFluidInTank(0).isEmpty() && !fluidResult.isEmpty() &&
                        !FluidStack.isSameFluidSameComponents(entity.getFluidTank().getFluidInTank(0), fluidResult)
                && entity.getFluidTank().getFluidInTank(0).getAmount() > fluidIngredient.getAmount())
                    continue;
                if (entity.getFluidTank().getFluidInTank(0).getAmount() + fluidResult.getAmount() - fluidIngredient.getAmount() > entity.getFluidTank().getTankCapacity(0))
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
}
