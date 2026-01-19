package com.minecart.central_heater.block_entity.misc;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SturdyTankBlockEntity extends BlockEntity {
    public static int MAX_FLUID_CAPACITY = 500;

    @Nullable
    private Component name;

    protected FluidTank tank;
    private final LazyOptional<IFluidHandler> fluidCap;

    public SturdyTankBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.sturdy_tank.get(), pos, blockState);
        this.name = getDefaultName();
        this.tank = new FluidTank(MAX_FLUID_CAPACITY) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };
        this.fluidCap = LazyOptional.of(() -> tank);
    }

    // --- Capabilities (Forge 1.20.1) ---
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidCap.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        tank.readFromNBT(tag);
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tank.writeToNBT(tag);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDefaultName() {
        return Component.translatable("container.sturdy_tank");
    }

    public FluidTank getTank() {
        return tank;
    }
}