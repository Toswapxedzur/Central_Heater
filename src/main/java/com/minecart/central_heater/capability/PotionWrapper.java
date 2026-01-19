package com.minecart.central_heater.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class PotionWrapper implements IFluidHandlerItem {
    protected ItemStack container;

    public PotionWrapper(ItemStack container){
        this.container = container;
    }

    public ItemStack getContainer() {
        return container;
    }

    public FluidStack getFluid(){
        if(container.getItem() != Items.POTION)
            return FluidStack.EMPTY;

        FluidStack stack = new FluidStack(Fluids.WATER, 250);
        if (container.hasTag()) {
            stack.setTag(container.getTag().copy());
        }
        return stack;
    }

    protected void setFluid(FluidStack stack){
        if(stack.isEmpty()){
            this.container = new ItemStack(Items.GLASS_BOTTLE);
        }
        else if(stack.getFluid() == Fluids.WATER){
            this.container = new ItemStack(Items.POTION);
            if (stack.hasTag()) {
                this.container.setTag(stack.getTag().copy());
            }
        }
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid() == Fluids.WATER;
    }

    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return 250;
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return fluidStack.isEmpty() || fluidStack.getFluid() == Fluids.WATER;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (this.container.getCount() == 1 && resource.getAmount() >= 250 && this.getFluid().isEmpty() && this.canFillFluidType(resource)) {
            if (action.execute()) {
                this.setFluid(resource);
            }
            return 250;
        } else {
            return 0;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (this.container.getCount() == 1 && resource.getAmount() >= 250) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
                if (action.execute()) {
                    this.setFluid(FluidStack.EMPTY);
                }
                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (this.container.getCount() == 1 && maxDrain >= 250) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty()) {
                if (action.execute()) {
                    this.setFluid(FluidStack.EMPTY);
                }

                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }
}