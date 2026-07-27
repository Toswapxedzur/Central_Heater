package com.minecart.central_heater.client;

import com.minecart.central_heater.heat.debug.HeatDebugLabel;
import com.minecart.central_heater.heat.debug.HeatDebugOutline;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHeatDebugState {
    private static final CopyOnWriteArrayList<HeatDebugLabel> LABELS = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<HeatDebugOutline> OUTLINES = new CopyOnWriteArrayList<>();
    private static volatile boolean enabled;

    private ClientHeatDebugState() {
    }

    public static void apply(boolean show, List<HeatDebugLabel> labels, List<HeatDebugOutline> outlines) {
        enabled = show;
        LABELS.clear();
        OUTLINES.clear();
        if (show) {
            LABELS.addAll(labels);
            OUTLINES.addAll(outlines);
        }
    }

    public static boolean enabled() {
        return enabled;
    }

    public static List<HeatDebugLabel> labels() {
        return LABELS;
    }

    public static List<HeatDebugOutline> outlines() {
        return OUTLINES;
    }
}
