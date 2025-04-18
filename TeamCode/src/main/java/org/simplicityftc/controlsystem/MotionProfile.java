package org.simplicityftc.controlsystem;

/*
SimplicityFTC's Motion Profile System uses an internal timer that is instantiated
when the class is created.
 */

import com.qualcomm.robotcore.util.ElapsedTime;

public class MotionProfile {
    private final double startPosition;
    private final double startVelocity;
    private final double targetPosition;
    private final ProfileConstraints constraints;
    private final ElapsedTime timeSinceMotionProfileCreation;

    public MotionProfile(double startPosition,
                         double targetPosition,
                         ProfileConstraints constraints) {
        this(startPosition, 0, targetPosition, constraints);
    }

    public MotionProfile(
            double startPosition,
            double startVelocity,
            double targetPosition,
            ProfileConstraints constraints) {
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.targetPosition = targetPosition;
        this.constraints = constraints;
        timeSinceMotionProfileCreation = new ElapsedTime();
    }

    public double getCurrentTargetPosition() {
        return 0;
    }

    public double getCurrentTargetVelocity() {
        return 0;
    }

    public double getCurrentTargetAcceleration() {
        return 0;
    }
}