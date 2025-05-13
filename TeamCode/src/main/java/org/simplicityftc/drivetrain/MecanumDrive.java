package org.simplicityftc.drivetrain;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.simplicityftc.controlsystem.PDFSController;
import org.simplicityftc.drivetrain.follower.Drivetrain;
import org.simplicityftc.drivetrain.follower.Follower;
import org.simplicityftc.drivetrain.follower.PidToPointFollower;
import org.simplicityftc.electronics.SimpleMotor;
import org.simplicityftc.electronics.SimpleVoltageSensor;
import org.simplicityftc.drivetrain.localizer.Localizer;
import org.simplicityftc.util.math.Pose;
import org.simplicityftc.util.math.SimpleMath;

public class MecanumDrive implements Drivetrain {
    private Drivetrain.DriveMode driveMode = DrivetrainSettings.driveMode;
    private final Localizer localizer = DrivetrainSettings.localizer;
    private final Follower follower = new PidToPointFollower();

    private boolean headingManuallyControlled = false;
    private final ElapsedTime headingTimer = new ElapsedTime();
    private double headingVelocity = 0;
    private Pose lastPose = new Pose();
    private double targetHeading = 0;

    public MecanumDrive() {
        leftFront = new SimpleMotor(DrivetrainSettings.DRIVETRAIN_MOTORS_HUB, DrivetrainSettings.leftFrontMotorPort);
        rightFront = new SimpleMotor(DrivetrainSettings.DRIVETRAIN_MOTORS_HUB, DrivetrainSettings.rightFrontMotorPort);
        leftRear = new SimpleMotor(DrivetrainSettings.DRIVETRAIN_MOTORS_HUB, DrivetrainSettings.leftRearMotorPort);
        rightRear = new SimpleMotor(DrivetrainSettings.DRIVETRAIN_MOTORS_HUB, DrivetrainSettings.rightRearMotorPort);

        leftFront.setReversed(DrivetrainSettings.reverseLeftFrontMotor);
        rightFront.setReversed(DrivetrainSettings.reverseRightFrontMotor);
        leftRear.setReversed(DrivetrainSettings.reverseLeftRearMotor);
        rightRear.setReversed(DrivetrainSettings.reverseRightRearMotor);

        headingController = new PDFSController(DrivetrainSettings.headingConstants);
    }

    public PDFSController headingController;

    private final SimpleMotor leftFront;
    private final SimpleMotor rightFront;
    private final SimpleMotor leftRear;
    private final SimpleMotor rightRear;

    public void setDriveMode(Drivetrain.DriveMode driveMode) {
        this.driveMode = driveMode;
        if (driveMode != Drivetrain.DriveMode.AUTONOMOUS && DrivetrainSettings.coastInTeleop) {
            leftFront.setCoasting(true);
            rightFront.setCoasting(true);
            leftRear.setCoasting(true);
            rightRear.setCoasting(true);
        } else {
            leftFront.setCoasting(false);
            rightFront.setCoasting(false);
            leftRear.setCoasting(false);
            rightRear.setCoasting(false);
        }
    }

    public Pose getPosition() {
        return localizer.getPose();
    }

    public void setPosition(Pose pose) {
        localizer.setPose(pose);
    }

    public Pose getVelocity() {
        return localizer.getVelocity();
    }

    public void drive(double x, double y, double heading) {
        x = SimpleMath.clamp(x, -1, 1);
        y = SimpleMath.clamp(y, -1, 1);
        heading = SimpleMath.clamp(heading, -1, 1);

        x *= DrivetrainSettings.translationalMaxPower;
        y *= DrivetrainSettings.translationalMaxPower;
        heading *= DrivetrainSettings.rotationalMaxPower;

        if (driveMode == Drivetrain.DriveMode.FIELD_CENTRIC) {
            double rotated_x = x * Math.cos(localizer.getPose().getHeading())
                             - y * Math.sin(localizer.getPose().getHeading());
            double rotated_y = x * Math.sin(localizer.getPose().getHeading())
                             + y * Math.cos(localizer.getPose().getHeading());
            x = rotated_x;
            y = rotated_y;
        }

        if (heading != 0) {
            headingManuallyControlled = true;
            //heading += K_STATIC*Math.signum(heading); //compensate for static friction for more precise control?
        } else if (DrivetrainSettings.headingLock) {
            if (headingVelocity < Math.toRadians(10) && headingManuallyControlled) {
                headingManuallyControlled = false;
                targetHeading = localizer.getPose().getHeading();
            }
            heading = headingController.calculate(localizer.getPose().getHeading(), targetHeading);
        }

        double denominator = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(heading), 1);

        leftFront.setPower((x - y - heading) / denominator);
        rightFront.setPower((x + y + heading) / denominator);
        leftRear.setPower((x + y - heading) / denominator);
        rightRear.setPower((x - y + heading) / denominator);
    }

    public void setMotorPowers(
                            double leftFront,
                            double rightFront,
                            double leftRear,
                            double rightRear) {
        this.leftFront.setPower(leftFront);
        this.rightFront.setPower(rightFront);
        this.leftRear.setPower(leftRear);
        this.rightRear.setPower(rightRear);
    }

    public void update() {
        headingController.setConstants(DrivetrainSettings.headingConstants);

        headingVelocity = (lastPose.getHeading() - localizer.getPose().getHeading()) / headingTimer.seconds();
        headingTimer.reset();

        localizer.update();
        lastPose = localizer.getPose();

        if (driveMode == Drivetrain.DriveMode.AUTONOMOUS) {
            Pose followVector = follower.getFollowVector().scale(12 / SimpleVoltageSensor.getVoltage());
            drive(followVector.getX(), followVector.getY(), followVector.getHeading());
        }

        leftFront.update();
        rightFront.update();
        leftRear.update();
        rightRear.update();
    }
}
