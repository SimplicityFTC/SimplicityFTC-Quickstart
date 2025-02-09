package org.simplicityftc.electronics;

//TODO: possibly mix cr and normal servos? use PWM instead of (0, 1) range?
public class Servo {
    private final int port;
    private final Hub hub;

    private double lastPosition = -1e7;
    private final double positionSetTolerance = 0.0;

    private boolean shouldUpdate = true;

    public Servo(Hub hub, int port) {
        if(port < 0 || port > 5) throw new IllegalArgumentException("Port must be between 0 and 5");
        this.hub = hub;
        this.port = port;
    }

    public void setPosition(double position) {
        position = Math.min(Math.max(position, -1), 1);

        if(Math.abs(position - lastPosition) < positionSetTolerance)
            return;

        lastPosition = position;
        shouldUpdate = true;
    }

    public double getTargetPosition() {
        return lastPosition;
    }

    public void update() {

        if(!shouldUpdate) {
            return;
        }

        hub.getLynxServoController().setServoPosition(port, lastPosition);
    }
}
