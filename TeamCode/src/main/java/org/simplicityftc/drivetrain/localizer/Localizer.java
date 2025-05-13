package org.simplicityftc.drivetrain.localizer;

import org.simplicityftc.util.math.Pose;

public interface Localizer {
    Pose getPose();
    Pose getVelocity();
    void setPose(Pose pose);
    void update();
}
