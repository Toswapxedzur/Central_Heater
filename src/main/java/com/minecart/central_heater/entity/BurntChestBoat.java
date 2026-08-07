package com.minecart.central_heater.entity;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BurntChestBoat extends ChestBoat {
    public BurntChestBoat(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    public BurntChestBoat(Level level, double x, double y, double z) {
        this(AllEntity.BURNT_CHEST_BOAT.get(), level);
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
