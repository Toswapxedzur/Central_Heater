package com.minecart.central_heater.entity;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.block.misc.BlazingFurnaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MinecartBlazingFurnace extends AbstractMinecart {
    private static final EntityDataAccessor<Boolean> DATA_ID_HAS_FUEL = SynchedEntityData.defineId(MinecartBlazingFurnace.class, EntityDataSerializers.BOOLEAN);
    // Ingredient assumes items are registered. If this crashes, move initialization to constructor.
    private static final Ingredient INGREDIENT = Ingredient.of(AllBlockItem.SCORCHED_COAL.get());

    public int fuel;
    public double xPush;
    public double zPush;

    public MinecartBlazingFurnace(EntityType<? extends AbstractMinecart> type, Level level) {
        super(type, level);
    }

    public MinecartBlazingFurnace(Level level, double x, double y, double z) {
        super(AllEntity.BLAZING_FURNACE_MINECART.get(), level, x, y, z);
    }

    @Override
    public Type getMinecartType() {
        return Type.FURNACE;
    }

    // 1.20.1 Syntax: No Builder argument
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_HAS_FUEL, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.fuel > 0) {
                --this.fuel;
            }

            if (this.fuel <= 0) {
                this.xPush = 0.0D;
                this.zPush = 0.0D;
            }

            this.setHasFuel(this.fuel > 0);
        }

        if (this.hasFuel()) {
            if(this.random.nextInt(3) == 0)
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY() + 0.8D, this.getZ(), 0.0D, 0.0D, 0.0D);
            if(this.random.nextInt(2) == 0)
                for(int i=0;i<this.random.nextInt(2);i++)
                    this.level().addParticle(ParticleTypes.SOUL, this.getX(), this.getY() + 0.8D, this.getZ(), 0.0D, 0.0D, 0.0D);
            if(this.random.nextInt(2) == 0)
                for(int i=0;i<this.random.nextInt(2);i++)
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.8D, this.getZ(), 0.0D, 0.0D, 0.0D);
            if(this.random.nextInt(3) == 0)
                for(int i=0;i<this.random.nextInt(2);i++)
                    this.level().addParticle(ParticleTypes.WHITE_ASH, this.getX(), this.getY() + 0.8D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected double getMaxSpeed() {
        return (this.isInWater() ? (double)18.0F : (double)24.0F) / (double)20.0F;
    }

    @Override
    protected Item getDropItem() {
        return AllBlockItem.BLAZING_FURNACE_MINECART.get();
    }

    @Override
    protected void moveAlongTrack(BlockPos pos, BlockState state) {
        double d0 = 1.0E-4;
        double d1 = 0.001;
        super.moveAlongTrack(pos, state);
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = vec3.horizontalDistanceSqr();
        double d3 = this.xPush * this.xPush + this.zPush * this.zPush;
        if (d3 > 1.0E-4 && d2 > 0.001) {
            double d4 = Math.sqrt(d2);
            double d5 = Math.sqrt(d3);
            this.xPush = vec3.x / d4 * d5;
            this.zPush = vec3.z / d4 * d5;
        }
    }

    @Override
    protected void applyNaturalSlowdown() {
        double d0 = this.xPush * this.xPush + this.zPush * this.zPush;

        if (d0 > 1.0E-7) {
            d0 = Math.sqrt(d0);
            this.xPush /= d0;
            this.zPush /= d0;

            Vec3 vec3 = this.getDeltaMovement().multiply(0.99D, 0.0D, 0.99D).add(this.xPush, 0.0D, this.zPush);

            if (this.isInWater()) {
                vec3 = vec3.scale(0.1D);
            }
            this.setDeltaMovement(vec3);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.998D, 0.0D, 0.998D));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult ret = super.interact(player, hand);
        if (ret.consumesAction()) {
            return ret;
        } else {
            ItemStack itemstack = player.getItemInHand(hand);
            if (INGREDIENT.test(itemstack) && this.fuel + 3600 <= 32000) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.fuel += 3600;
            }

            if (this.fuel > 0) {
                this.xPush = this.getX() - player.getX();
                this.zPush = this.getZ() - player.getZ();
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
    }

    @Override
    public float getMaxCartSpeedOnRail() {
        return 1.2F;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("PushX", this.xPush);
        compound.putDouble("PushZ", this.zPush);
        compound.putShort("Fuel", (short)this.fuel);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.xPush = compound.getDouble("PushX");
        this.zPush = compound.getDouble("PushZ");
        this.fuel = compound.getShort("Fuel");
    }

    protected boolean hasFuel() {
        return this.entityData.get(DATA_ID_HAS_FUEL);
    }

    protected void setHasFuel(boolean hasFuel) {
        this.entityData.set(DATA_ID_HAS_FUEL, hasFuel);
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return AllBlockItem.BLAZING_FURNACE.get().defaultBlockState().setValue(FurnaceBlock.FACING, Direction.NORTH).setValue(BlazingFurnaceBlock.LIT, this.hasFuel());
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(AllBlockItem.BLAZING_FURNACE_MINECART.get());
    }
}