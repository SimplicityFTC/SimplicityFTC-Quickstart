package org.simplicityftc.follower.localizer;

import org.simplicityftc.util.math.Pose;

public class PinpointLocalizer implements Localizer {

    private Pose lastPose = new Pose();

    @Override
    public Pose getPose() {
        return lastPose;
    }

    @Override
    public Pose getVelocity() {
        return null;
    }

    @Override
    public void setPose(Pose pose) {
        lastPose = pose;
    }

    @Override
    public void update() {

    }
}
