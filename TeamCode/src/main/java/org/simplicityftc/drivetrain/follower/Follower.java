package org.simplicityftc.drivetrain.follower;

import org.simplicityftc.util.math.Pose;

public interface Follower {
    Follower add(Pose... points);
    Follower clear();
    boolean atTarget();
    Pose getFollowVector();
}
