package com.minecart.central_heater.misc.enumeration;

import net.minecraft.util.StringRepresentable;

import java.util.function.Function;

public enum NetherFireState implements StringRepresentable {
    NONE("none", 0),
    BURN("burn", 1),
    SOUL("soul", 2);
    private final String name;
    private final int state;
    NetherFireState(String name, int state){
        this.name = name;
        this.state = state;
    }

    public static final Function<String, NetherFireState> func = StringRepresentable.createNameLookup(values(), s -> s);

    @Override
    public String toString() { return name; }

    @Override
    public String getSerializedName() { return name; }

    public int getState(){ return state; }
}
