package org.simplicityftc.follower;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.simplicityftc.electronics.Hub;
import org.simplicityftc.electronics.Motor;
import org.simplicityftc.controlsystem.PDFSController;
import org.simplicityftc.follower.localizer.Localizer;
import org.simplicityftc.electronics.SimpleVoltageSensor;
import org.simplicityftc.util.math.Pose;

public class MecanumDrive {
    public enum DriveMode {
        FIELD_CENTRIC,
        ROBOT_CENTRIC,
        AUTONOMOUS
    }

    private DriveMode driveMode = DrivetrainSettings.driveMode;
    private final Localizer localizer = DrivetrainSettings.localizer;

    private boolean headingManuallyControlled = false;
    private final ElapsedTime headingTimer = new ElapsedTime();
    private double headingVelocity = 0;
    private Pose lastPose = new Pose();
    private double targetHeading = 0;

    public MecanumDrive() {
        leftFront = new Motor(Hub.CONTROL_HUB, DrivetrainSettings.leftFrontMotorPort);
        rightFront = new Motor(Hub.CONTROL_HUB, DrivetrainSettings.rightFrontMotorPort);
        leftRear = new Motor(Hub.CONTROL_HUB, DrivetrainSettings.leftRearMotorPort);
        rightRear = new Motor(Hub.CONTROL_HUB, DrivetrainSettings.rightRearMotorPort);

        leftFront.setReversed(DrivetrainSettings.reverseLeftFrontMotor);
        rightFront.setReversed(DrivetrainSettings.reverseRightFrontMotor);
        leftRear.setReversed(DrivetrainSettings.reverseLeftRearMotor);
        rightRear.setReversed(DrivetrainSettings.reverseRightRearMotor);

        forwardController = new PDFSController(DrivetrainSettings.xConstants);
        strafeController = new PDFSController(DrivetrainSettings.yConstants);
        headingController = new PDFSController(DrivetrainSettings.headingConstants);
    }

    public PDFSController forwardController;
    public PDFSController strafeController;
    public PDFSController headingController;

    private final Motor leftFront;
    private final Motor rightFront;
    private final Motor leftRear;
    private final Motor rightRear;

    public void setDriveMode(DriveMode driveMode) {
        this.driveMode = driveMode;
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
        x *= DrivetrainSettings.translationalMaxPower;
        y *= DrivetrainSettings.translationalMaxPower;
        heading *= DrivetrainSettings.rotationalMaxPower;

        if(driveMode == DriveMode.FIELD_CENTRIC) {
            double rotated_x = x * Math.cos(localizer.getPose().getHeading()) - y * Math.sin(localizer.getPose().getHeading());
            double rotated_y = x * Math.sin(localizer.getPose().getHeading()) + y * Math.cos(localizer.getPose().getHeading());
            x = rotated_x;
            y = rotated_y;
        }

        if(heading != 0) {
            headingManuallyControlled = true;
            //heading += K_STATIC*Math.signum(heading); //compensate for static friction for more precise control?
        } else if(DrivetrainSettings.headingLock) {
            if(headingVelocity < Math.toRadians(10) && headingManuallyControlled) {
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

    public void setMotorPowers(double leftFront, double rightFront, double leftRear, double rightRear) {
        this.leftFront.setPower(leftFront);
        this.rightFront.setPower(rightFront);
        this.leftRear.setPower(leftRear);
        this.rightRear.setPower(rightRear);
    }

    public void update() {
        forwardController.setConstants(DrivetrainSettings.xConstants);
        strafeController.setConstants(DrivetrainSettings.yConstants);
        headingController.setConstants(DrivetrainSettings.headingConstants);

        headingVelocity = (lastPose.getHeading() - localizer.getPose().getHeading()) / headingTimer.seconds();
        headingTimer.reset();

        localizer.update();
        lastPose = localizer.getPose();

        if(driveMode == DriveMode.AUTONOMOUS) {
            Pose targetPose = new Pose();

            double fieldCentricXError = targetPose.sub(this.getPosition()).getX();
            double fieldCentricYError = targetPose.sub(this.getPosition()).getY();

            double robotCentricForwardError = Math.cos(-this.getPosition().getHeading()) * fieldCentricXError -
                                                Math.sin(-this.getPosition().getHeading()) * fieldCentricYError;
            double robotCentricStrafeError =  Math.sin(-this.getPosition().getHeading()) * fieldCentricXError -
                                                Math.cos(-this.getPosition().getHeading()) * fieldCentricYError;
            double headingTarget = 0;

            forwardController.setTarget(0);
            strafeController.setTarget(0);
            headingController.setTarget(headingTarget);

            double forwards_power = forwardController.calculate(-robotCentricForwardError);
            double strafe_power = strafeController.calculate(-robotCentricStrafeError);
            double headingPower = headingController.calculate(headingTarget + AngleUnit.normalizeRadians(headingTarget - getPosition().getHeading()));

            double denominator = Math.max(Math.abs(forwards_power) + Math.abs(strafe_power) + Math.abs(headingPower), 1);

            double leftFrontPower = (forwards_power - strafe_power - headingPower) / denominator;
            double rightFrontPower = (forwards_power + strafe_power + headingPower) / denominator;
            double leftRearPower = (forwards_power + strafe_power - headingPower) / denominator;
            double rightRearPower = (forwards_power - strafe_power + headingPower) / denominator;

            if(targetPose.sub(getPosition()).magnitude() > 2) { //TODO: tune this threshold
                leftFrontPower = (leftFrontPower + Math.signum(leftFrontPower) * DrivetrainSettings.K_STATIC);
                rightFrontPower = (rightFrontPower + Math.signum(rightFrontPower) * DrivetrainSettings.K_STATIC);
                leftRearPower = (leftRearPower + Math.signum(leftRearPower) * DrivetrainSettings.K_STATIC);
                rightRearPower = (rightRearPower + Math.signum(rightRearPower) * DrivetrainSettings.K_STATIC);
            }

            leftFront.setPower(leftFrontPower * 12 / SimpleVoltageSensor.getVoltage());
            rightFront.setPower(rightFrontPower * 12 / SimpleVoltageSensor.getVoltage());
            leftRear.setPower(leftRearPower * 12 / SimpleVoltageSensor.getVoltage());
            rightRear.setPower(rightRearPower * 12 / SimpleVoltageSensor.getVoltage());
        }

        leftFront.update();
        rightFront.update();
        leftRear.update();
        rightRear.update();
    }
}
