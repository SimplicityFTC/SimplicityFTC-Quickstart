package org.simplicityftc.drivetrain.path;

import org.simplicityftc.util.math.Pose;

public interface Path {
    Pose getClosestPose(Pose pose);
    Pose getDistanceFromEnd(Pose pose);
    Pose getEndPose();
}
