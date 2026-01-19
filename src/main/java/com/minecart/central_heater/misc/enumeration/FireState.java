package com.minecart.central_heater.misc.enumeration;

import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FireState implements StringRepresentable {
    NONE("none", false),
    LIT("lit", true);
    private final String name;
    private final boolean state;
    FireState(String name, boolean state){
        this.name = name;
        this.state  = state;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    private static final Map<String, FireState> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(FireState::getSerializedName, i -> i));
    public static final Function<String, FireState> func = BY_NAME::get;

    public static FireState getFireState(boolean state){
        return state ? FireState.LIT : FireState.NONE;
    }

    public boolean getBooleanState(){
        return this.state;
    }
}