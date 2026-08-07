package com.minecart.central_heater.entity;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BurntBoat extends Boat {
    public BurntBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public BurntBoat(Level level, double x, double y, double z) {
        this(AllEntity.BURNT_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public Item getDropItem() {
        return AllBlockItem.BURNT_PLANKS.get().asItem();
    }

    @Override
    public Boat.Type getVariant() {
        return Boat.Type.OAK;
    }
}
