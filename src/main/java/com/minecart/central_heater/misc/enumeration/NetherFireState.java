package com.minecart.central_heater.misc.enumeration;

import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private static final Map<String, NetherFireState> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(NetherFireState::getSerializedName, s -> s));
    public static final Function<String, NetherFireState> func = BY_NAME::get;

    @Override
    public String toString() { return name; }

    @Override
    public String getSerializedName() { return name; }

    public int getState(){ return state; }
}