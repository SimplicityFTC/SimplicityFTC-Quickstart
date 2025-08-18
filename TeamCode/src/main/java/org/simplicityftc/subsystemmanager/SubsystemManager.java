package org.simplicityftc.subsystemmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsystemManager {
    private static final SubsystemManager INSTANCE = new SubsystemManager();
    private List<Subsystem> subsystems;

    private SubsystemManager() {
        subsystems = new ArrayList<>();
    }

    public static SubsystemManager getInstance() {
        return INSTANCE;
    }

    public void add(Subsystem... subsystem) {
        subsystems.addAll(Arrays.asList(subsystem));
    }

    public void clear() {
        subsystems = new ArrayList<>();
    }

    public void run() {
        int timestamp = (int) (System.nanoTime() * 1E-6);

        for (Subsystem subsystem : subsystems) {
            if (timestamp - subsystem.getLastReadTimestamp() >= subsystem.getReadIntervalMs()) {
                subsystem.read();
                subsystem.setLastReadTimestamp(timestamp);
            }
            if (timestamp - subsystem.getLastComputeTimestamp() >= subsystem.getComputeIntervalMs()) {
                subsystem.compute();
                subsystem.setLastComputeTimestamp(timestamp);
            }
            if (timestamp - subsystem.getLastWriteTimestamp() >= subsystem.getWriteIntervalMs()) {
                subsystem.write();
                subsystem.setLastWriteTimestamp(timestamp);
            }
        }
    }
}
