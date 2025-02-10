package org.simplicityftc.electronics;

public class SimpleEncoder {
    private final int port;
    private final Hub hub;

    private boolean isReversed = false;
    private double positionOffset = 0;

    public SimpleEncoder(Hub hub, int port) {
        if(port < 0 || port > 3) throw new IllegalArgumentException("Port must be between 0 and 3");
        this.hub = hub;
        this.port = port;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    /**
     * @param newPosition new position in ticks
     */
    public void setPosition(double newPosition) {
        newPosition *= (isReversed ? -1 : 1);
        positionOffset = newPosition - hub.getBulkData().getEncoder(port) * (isReversed ? -1 : 1);
    }

    /**
     * @return encoder position in ticks
     */
    public double getPosition() {
        return hub.getBulkData().getEncoder(port) * (isReversed ? -1 : 1) + positionOffset;
    }

    /**
     * @return encoder velocity in ticks/s
     */
    public double getVelocity() { //TODO: move to overflow compensated velocity
        return hub.getBulkData().getVelocity(port) * (isReversed ? -1 : 1);
    }
}
