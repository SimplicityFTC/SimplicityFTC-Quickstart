package org.simplicityftc.drivetrain.follower;

import org.simplicityftc.controlsystem.PDFSController;
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
    private final List<Pose> points = new ArrayList<>();
    private final Drivetrain drivetrain;

    public PidToPointFollower(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        points.clear();
        forwardController = new PDFSController(drivetrain.forwardConstants);
        forwardController.setTarget(0);
        strafeController = new PDFSController(drivetrain.strafeConstants);
        strafeController.setTarget(0);
        headingController = new PDFSController(drivetrain.headingConstants);
    }

    private Pose getPosition() {
        return drivetrain.localizer.getPose();
    };

    @Override
    public Follower add(Pose... points) {
        Arrays.stream(points).sequential().forEach(this.points::add);
        return this;
    }

    @Override
    public Follower clear() {
        points.clear();
        return this;
    }

    public boolean atTarget() {
        if (points.isEmpty()) return true;
        return points.get(0).sub(getPosition()).magnitude() < drivetrain.followerTolerance &&
                Math.abs(SimpleMath.normalizeRadians(points.get(0).getHeading() - getPosition().getHeading())) < Math.toRadians(5);
    }

    @Override
    public Pose getFollowVector() {
        while (points.get(0).sub(drivetrain.localizer.getPose()).magnitude() < pointAdvanceRadius &&
                Math.abs(SimpleMath.normalizeRadians(points.get(0).getHeading() - getPosition().getHeading())) < Math.toRadians(10) &&
                points.size() > 1) {
            points.remove(0);
        }

        Pose robotCentricTarget = points.get(0).rotate(-drivetrain.localizer.getPose().getHeading());

        double headingTarget = 0;

        forwardController.setTarget(robotCentricTarget.getX());
        strafeController.setTarget(robotCentricTarget.getY());
        headingController.setTarget(SimpleMath.normalizeRadians(headingTarget - getPosition().getHeading()));

        return new Pose(
                forwardController.calculate(drivetrain.localizer.getPose().getX()),
                strafeController.calculate(drivetrain.localizer.getPose().getY()),
                headingController.calculate(getPosition().getHeading()));
    }
}
