package com.minecart.central_heater.heat.sim;

import com.minecart.central_heater.heat.api.HeatPort;
import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.api.HeatUnits;
import com.minecart.central_heater.heat.storage.ThermalGroup;

public class HeatTransferMath {
    private static final long CONTACT_TRANSFER_DENOMINATOR = 32768L;
    private static final long GLOBAL_TRANSFER_BOOST = 2L;

    private HeatTransferMath() {
    }

    public static long proposeTransferEnergy(int fromTemperature, int toTemperature, ThermalGroup from, ThermalGroup to, HeatProfile fromProfile, HeatProfile toProfile, HeatPort fromPort, HeatPort toPort, int contactArea, int transferBoost, int heatSteps) {
        long diff = (long) fromTemperature - toTemperature;
        if (diff <= 0 || contactArea <= 0 || !fromPort.open() || !toPort.open()) {
            return 0L;
        }
        long stepMultiplier = Math.max(1L, heatSteps);
        long conductivity = Math.min(Math.min(fromProfile.conductivity(), toProfile.conductivity()), Math.min(fromPort.conductivity(), toPort.conductivity()));
        long insulation = (long) fromProfile.insulation() + toProfile.insulation() + fromPort.insulation() + toPort.insulation();
        long maxTransfer = Math.min(Math.min(fromProfile.maxTransfer(), toProfile.maxTransfer()), Math.min(fromPort.maxTransfer(), toPort.maxTransfer()));
        long minCapacity = Math.max(1L, Math.min(from.capacity(), to.capacity()));
        long proposed = diff * conductivity * Math.max(1, contactArea) * Math.max(1, transferBoost) * GLOBAL_TRANSFER_BOOST * stepMultiplier * minCapacity / Math.max(4096L, CONTACT_TRANSFER_DENOMINATOR + insulation * 128L);
        long maxTransferEnergy = maxTransfer * HeatUnits.TEMPERATURE_SCALE * minCapacity * GLOBAL_TRANSFER_BOOST * stepMultiplier;
        long equilibriumEnergy = diff * from.capacity() * to.capacity() / Math.max(1L, (long) from.capacity() + to.capacity());
        proposed = Math.min(proposed, maxTransferEnergy);
        proposed = Math.min(proposed, equilibriumEnergy);
        return Math.max(0L, proposed);
    }
}
