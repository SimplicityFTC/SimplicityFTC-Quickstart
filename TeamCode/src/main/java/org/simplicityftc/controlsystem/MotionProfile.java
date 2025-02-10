package org.simplicityftc.controlsystem;

public class MotionProfile {
    private final double startPosition;
    private final double targetPosition;
    private final ProfileConstraints constraints;

    public MotionProfile(double startPosition, double targetPosition, ProfileConstraints constraints) {
        this.startPosition = startPosition;
        this.targetPosition = targetPosition;
        this.constraints = constraints;
    }

    public double getPosition() {
        return 0;
    }
    public double getVelocity() {
        return 0;
    }
    public double getAcceleration() {
        return 0;
    }
}