package org.simplicityftc.follower.path;

import org.simplicityftc.util.math.Pose;

public interface Path {
    Pose getClosestPose(Pose pose);
    Pose getDistanceFromEnd(Pose pose);
    Pose getEndPose();
}
