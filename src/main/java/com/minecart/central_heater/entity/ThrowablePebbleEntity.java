package com.minecart.central_heater.entity;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrowablePebbleEntity extends ThrowableItemProjectile {
    public ThrowablePebbleEntity(EntityType<? extends ThrowablePebbleEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowablePebbleEntity(Level level, double x, double y, double z) {
        super(AllEntity.PEBBLE.get(), x, y, z, level);
    }

    public ThrowablePebbleEntity(Level level, LivingEntity shooter){
        super(AllEntity.PEBBLE.get(), shooter, level);
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        // RegistryObject in 1.20.1 does not have .toStack(), must create manually
        return (ParticleOptions)(!itemstack.isEmpty() && !itemstack.is(this.getDefaultItem()) ?
                new ItemParticleOption(ParticleTypes.ITEM, itemstack) :
                new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(AllBlockItem.COBBLE.get())));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particleoptions = this.getParticle();
            for(int i = 0; i < 12; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 4.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return AllBlockItem.COBBLE.get();
    }
}