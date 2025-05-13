package org.simplicityftc.drivetrain.follower;

import org.simplicityftc.controlsystem.PDFSController;
import org.simplicityftc.drivetrain.DrivetrainSettings;
import org.simplicityftc.util.math.Pose;
import org.simplicityftc.util.math.SimpleMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PidToPointFollower implements Follower {
    public static double pointAdvanceRadius = 4; //cm before it targets the next point

    private final PDFSController forwardController;
    private final PDFSController strafeController;
    private final PDFSController headingController;
    private static final List<Pose> points = new ArrayList<>();

    public PidToPointFollower() {
        points.clear();
        forwardController = new PDFSController(DrivetrainSettings.forwardConstants);
        forwardController.setTarget(0);
        strafeController = new PDFSController(DrivetrainSettings.strafeConstants);
        strafeController.setTarget(0);
        headingController = new PDFSController(DrivetrainSettings.headingConstants);
    }

    private Pose getPosition() {
        return DrivetrainSettings.localizer.getPose();
    };

    @Override
    public void add(Pose... points) {
        Arrays.stream(points).sequential().forEach(PidToPointFollower.points::add);
    }

    @Override
    public void clear() {
        points.clear();
    }

    public boolean atTarget() {
        if (points.isEmpty()) return true;
        return points.get(0).sub(getPosition()).magnitude() < DrivetrainSettings.followerTolerance &&
                Math.abs(SimpleMath.normalizeRadians(points.get(0).getHeading() - getPosition().getHeading())) < Math.toRadians(5);
    }

    @Override
    public Pose getFollowVector() {
        while (points.get(0).sub(DrivetrainSettings.localizer.getPose()).magnitude() < pointAdvanceRadius &&
                Math.abs(SimpleMath.normalizeRadians(points.get(0).getHeading() - getPosition().getHeading())) < Math.toRadians(10) &&
                points.size() > 1) {
            points.remove(0);
        }

        Pose robotCentricTarget = points.get(0).rotate(-DrivetrainSettings.localizer.getPose().getHeading());

        double headingTarget = 0;

        forwardController.setTarget(robotCentricTarget.getX());
        strafeController.setTarget(robotCentricTarget.getY());
        headingController.setTarget(SimpleMath.normalizeRadians(headingTarget - getPosition().getHeading()));

        return new Pose(
                forwardController.calculate(DrivetrainSettings.localizer.getPose().getX()),
                strafeController.calculate(DrivetrainSettings.localizer.getPose().getY()),
                headingController.calculate(getPosition().getHeading()));
    }
}
