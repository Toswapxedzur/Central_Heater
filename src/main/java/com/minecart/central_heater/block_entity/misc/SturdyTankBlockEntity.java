package com.minecart.central_heater.block_entity.misc;

import com.minecart.central_heater.item.AllDataComponents;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.heat.api.HeatBlockEntityBehavior;
import com.minecart.central_heater.heat.api.HeatSink;
import com.minecart.central_heater.heat.api.HeatType;
import com.minecart.central_heater.heat.context.HeatNodeAccess;
import com.minecart.central_heater.heat.context.HeatNodeContext;
import com.minecart.central_heater.heat.storage.HeatNode;
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

public class SturdyTankBlockEntity extends BlockEntity implements HeatBlockEntityBehavior {
    public static int MAX_FLUID_CAPACITY = 500;

    @Nullable
    private Component name;

    protected FluidTank tank;

    public SturdyTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.STURDY_TANK.get(), pos, blockState);
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

    @Override
    public HeatNode createHeatNode() {
        return new HeatNode(getBlockPos(), 0, 1800, HeatType.NORMAL);
    }

    @Override
    public HeatSink getSink(HeatNodeContext ctx) {
        return tank.isEmpty() ? HeatSink.NONE : new HeatSink(3, 0, 3, false);
    }

    @Override
    public void tickHeatNode(HeatNodeContext ctx, HeatNodeAccess heat) {
        if (tank.isEmpty() && heat.getHeat() > 0) {
            heat.setHeat(Math.max(0, heat.getHeat() - 1));
        }
    }
}
