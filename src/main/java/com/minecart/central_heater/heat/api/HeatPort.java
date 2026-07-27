package com.minecart.central_heater.heat.api;

public record HeatPort(int conductivity, int insulation, int maxTransfer, boolean open) {
    public static final HeatPort CLOSED = new HeatPort(0, 1024, 0, false);

    public static HeatPort fromProfile(HeatProfile profile) {
        return new HeatPort(profile.conductivity(), profile.insulation(), profile.maxTransfer(), profile.conductivity() > 0);
    }
}
