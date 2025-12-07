package com.minecart.central_heater.util;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;

public class AllConstants {
    public static final EnumProperty<NetherFireState> LIT_SOUL = EnumProperty.create("lit_soul", NetherFireState.class);

    public static final Vec3[] stoveInvLoc1 = new Vec3[]{
            new Vec3(0.5, 1.12, 0.5)};

    public static final Vec3[] stoveInvLoc4 = new Vec3[]{
            new Vec3(0.25, 1.12, 0.25), new Vec3(0.25, 1.12, -0.25),
            new Vec3(-0.25, 1.12, 0.25), new Vec3(-0.25, 1.12, -0.25)};

    public static final Vec3[] stoveInvLoc9 = new Vec3[]{
            new Vec3(0.3, 1.12, 0.3), new Vec3(0.3, 1.12, 0), new Vec3(0.3, 1.12, -0.3),
            new Vec3(0, 1.12, 0.3), new Vec3(0, 1.12, 0), new Vec3(0, 1.12, -0.3),
            new Vec3(-0.3, 1.12, 0.3), new Vec3(-0.3, 1.12, 0), new Vec3(-0.3, 1.12, -0.3)};

    public static final Vec3[][] stoveFuelLoc4 = new Vec3[][]{
            {new Vec3(0, 0.3, 0)},
            {new Vec3(-0.15, 0.3, 0), new Vec3(0.15, 0.3, 0)},
            {new Vec3(-0.15, 0.3, -0.15), new Vec3(-0.15, 0.3, 0.15), new Vec3(0.15, 0.3, 0)},
            {new Vec3(-0.15, 0.3, -0.15), new Vec3(-0.15, 0.3, 0.15), new Vec3(0.15, 0.3, 0), new Vec3(0, 0.55, 0)}};

    public static final Vec3[] potInvLoc8 = new Vec3[]{
            new Vec3(0.15, 0.375f, 0.15), new Vec3(0.15, 0.375f, -0.15), new Vec3(-0.15, 0.375f, -0.15), new Vec3(-0.15, 0.375f, 0.15),
            new Vec3(0.25, 0.625f, 0), new Vec3(0, 0.625f, -0.25), new Vec3(-0.25, 0.625f, 0), new Vec3(0, 0.625f, 0.25)
    };

}
