package org.simplicityftc.drivetrain.follower;

import org.simplicityftc.util.math.Pose;

public interface Follower {
    void add(Pose... points);
    void clear();
    boolean atTarget();
    Pose getFollowVector();
}
