package com.minecart.central_heater.block_entity.misc;

import com.minecart.central_heater.item.AllDataComponents;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class SturdyTankBlockEntity extends BlockEntity {
    public static int MAX_FLUID_CAPACITY = 500;

    @Nullable
    private Component name;

    protected FluidTank tank;

    public SturdyTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.sturdy_tank.get(), pos, blockState);
        name = getDefaultName();
        tank = new FluidTank(MAX_FLUID_CAPACITY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.readFromNBT(registries, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tank.writeToNBT(registries, tag);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.name);
        components.set(AllDataComponents.SIMPLE_FLUID_CONTENT, SimpleFluidContent.copyOf(this.tank.getFluid()));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.name = componentInput.getOrDefault(DataComponents.CUSTOM_NAME, getDefaultName());
        this.tank.setFluid(componentInput.getOrDefault(AllDataComponents.SIMPLE_FLUID_CONTENT, SimpleFluidContent.EMPTY).copy());
    }

    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDefaultName() {
        return Component.translatable("container.sturdy_tank");
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
        tag.remove("simple_fluid_content");
    }

    public FluidTank getTank() {
        return tank;
    }
}
