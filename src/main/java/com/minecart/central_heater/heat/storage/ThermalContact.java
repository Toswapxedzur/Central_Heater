package com.minecart.central_heater.heat.storage;

import net.minecraft.core.Direction;

public record ThermalContact(int groupA, int groupB, Direction direction, int contactArea) {
}
