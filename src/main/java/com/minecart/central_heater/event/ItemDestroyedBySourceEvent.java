package com.minecart.central_heater.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.EntityEvent;

public class ItemDestroyedBySourceEvent extends EntityEvent{
    private final ItemEntity itemEntity;
    private final DamageSource damageSource;

    public ItemDestroyedBySourceEvent(ItemEntity itemEntity, DamageSource damageSource) {
        super(itemEntity);
        this.itemEntity = itemEntity;
        this.damageSource = damageSource;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }
}