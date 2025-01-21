package org.simplicityftc.follower.path;

import org.simplicityftc.util.math.Pose;

public class BezierPath implements Path{
    private final Pose startPose;
    private final Pose controlPoint1;
    private final Pose controlPoint2;
    private final Pose endPose;
    private double length = 0;

    public BezierPath(Pose startPose, Pose controlPoint1, Pose controlPoint2, Pose endPose) {
        this.startPose = startPose;
        this.controlPoint1 = controlPoint1;
        this.controlPoint2 = controlPoint2;
        this.endPose = endPose;

        for(int i = 0; i < 100; i++) {
            length += Math.sqrt(
                    (get(0.01 * i).sub(get(0.01 * (i+1)))).getX() * (get(0.01 * i).sub(get(0.01 * (i+1)))).getX() +
                    (get(0.01 * i).sub(get(0.01 * (i+1)))).getY() * (get(0.01 * i).sub(get(0.01 * (i+1)))).getY());
        }
    }

    private Pose get(double t){
        return endPose.scale(t * t * t).add(
                controlPoint2.scale(3.0 * (1.0 - t) * t * t)).add(
                controlPoint1.scale(3.0 * (1.0 - t) * (1.0 - t) * t)).add(
                startPose.scale((1.0 - t) * (1.0 - t) * (1.0 - t)));
    }

    @Override
    public Pose getClosestPose(Pose pose) {

        return new Pose();
    }

    @Override
    public Pose getDistanceFromEnd(Pose pose) {
        return new Pose();
    }

    @Override
    public Pose getEndPose() {
        return endPose;
    }

    private double getDx(double t) {
        double dx = (3 * endPose.getX() * t * t) + (6 * controlPoint2.getX() * t - 9 * controlPoint2.getX() * t * t) + (3 * controlPoint1.getX() - 6 * controlPoint1.getX() * t - 9 * controlPoint1.getX() * t * t);
        return dx;
    }

    private double getDy(double t) {
        double dy = (3 * endPose.getY() * t * t) + (6 * controlPoint2.getY() * t - 9 * controlPoint2.getY() * t * t) + (3 * controlPoint1.getY() - 6 * controlPoint1.getY() * t - 9 * controlPoint1.getY() * t * t);
        return dy;
    }
    private double getDxx(double t) {
        double dxx = (6 * endPose.getX() * t) + (6 * controlPoint2.getX() - 18 * controlPoint2.getX() * t) + (-6 * controlPoint1.getX());
        return dxx;
    }
}