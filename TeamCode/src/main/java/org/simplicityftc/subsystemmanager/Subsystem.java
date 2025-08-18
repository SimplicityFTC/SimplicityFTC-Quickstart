package org.simplicityftc.subsystemmanager;

public abstract class Subsystem {
    private int readIntervalMs = 250;
    private int computeIntervalMs = 0;
    private int writeIntervalMs = 0;
    private int lastReadTimestamp = 0;
    private int lastComputeTimestamp = 0;
    private int lastWriteTimestamp = 0;

    public Subsystem() {
        SubsystemManager.getInstance().add(this);
    }

    public abstract void read();
    public abstract void compute();
    public abstract void write();

    // These are accessible to users
    public void setReadIntervalMs(int readIntervalMs) {
        this.readIntervalMs = readIntervalMs;
    }

    public void setComputeIntervalMs(int computeIntervalMs) {
        this.computeIntervalMs = computeIntervalMs;
    }

    public void setWriteIntervalMs(int writeIntervalMs) {
        this.writeIntervalMs = writeIntervalMs;
    }

    // These are only accessible to the manager
    int getReadIntervalMs() {
        return readIntervalMs;
    }

    int getComputeIntervalMs() {
        return computeIntervalMs;
    }

    int getWriteIntervalMs() {
        return writeIntervalMs;
    }

    int getLastReadTimestamp() {
        return lastReadTimestamp;
    }

    int getLastComputeTimestamp() {
        return lastComputeTimestamp;
    }

    int getLastWriteTimestamp() {
        return lastWriteTimestamp;
    }

    void setLastComputeTimestamp(int lastComputeTimestamp) {
        this.lastComputeTimestamp = lastComputeTimestamp;
    }

    void setLastReadTimestamp(int lastReadTimestamp) {
        this.lastReadTimestamp = lastReadTimestamp;
    }

    void setLastWriteTimestamp(int lastWriteTimestamp) {
        this.lastWriteTimestamp = lastWriteTimestamp;
    }
}
