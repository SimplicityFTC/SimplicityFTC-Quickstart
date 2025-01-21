package org.simplicityftc.follower;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.simplicityftc.controlsystem.Motor;
import org.simplicityftc.controlsystem.PDFSController;
import org.simplicityftc.follower.localizer.Localizer;
import org.simplicityftc.util.math.Pose;

public class MecanumDrive {

    public enum DriveMode{
        FIELD_CENTRIC,
        ROBOT_CENTRIC,
        AUTONOMOUS
    }

    private DriveMode driveMode = DrivetrainSettings.driveMode;
    private final Localizer localizer = DrivetrainSettings.localizer;

    private boolean headingManuallyControlled = false;
    private ElapsedTime headingTimer = new ElapsedTime();
    private double headingVelocity = 0;
    private Pose lastPose = new Pose();
    private double targetHeading = 0;

    public MecanumDrive(HardwareMap hardwareMap){
        leftFront = new Motor(hardwareMap.get(DcMotor.class, DrivetrainSettings.leftFrontMotorName));
        rightFront = new Motor(hardwareMap.get(DcMotor.class, DrivetrainSettings.rightFrontMotorName));
        leftRear = new Motor(hardwareMap.get(DcMotor.class, DrivetrainSettings.leftRearMotorName));
        rightRear = new Motor(hardwareMap.get(DcMotor.class, DrivetrainSettings.rightRearMotorName));

        leftFront.setReversed(DrivetrainSettings.reverseLeftFrontMotor);
        rightFront.setReversed(DrivetrainSettings.reverseRightFrontMotor);
        leftRear.setReversed(DrivetrainSettings.reverseLeftRearMotor);
        rightRear.setReversed(DrivetrainSettings.reverseRightRearMotor);

        xController = new PDFSController(DrivetrainSettings.xConstants);
        yController = new PDFSController(DrivetrainSettings.yConstants);
        headingController = new PDFSController(DrivetrainSettings.headingConstants);
    }


    public PDFSController xController;
    public PDFSController yController;
    public PDFSController headingController;

    private final Motor leftFront;
    private final Motor rightFront;
    private final Motor leftRear;
    private final Motor rightRear;

    public void setDriveMode(DriveMode driveMode){
        this.driveMode = driveMode;
    }

    public void drive(double x, double y, double heading){
        if(driveMode == DriveMode.FIELD_CENTRIC) {
            double rotated_x = x * Math.cos(localizer.getPose().getHeading()) - y * Math.sin(localizer.getPose().getHeading());
            double rotated_y = x * Math.sin(localizer.getPose().getHeading()) + y * Math.cos(localizer.getPose().getHeading());
            x = rotated_x;
            y = rotated_y;
        }

        if(heading != 0){
            headingManuallyControlled = true;
            //heading += 0.1*Math.signum(heading); //compensate for static friction for more precise control
        }else if(DrivetrainSettings.headingLock){
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

    public void setMotorPowers(double leftFront, double rightFront, double leftRear, double rightRear){
        this.leftFront.setPower(leftFront);
        this.rightFront.setPower(rightFront);
        this.leftRear.setPower(leftRear);
        this.rightRear.setPower(rightRear);
    }

    public void update(){
        xController.setConstants(DrivetrainSettings.xConstants);
        yController.setConstants(DrivetrainSettings.yConstants);
        headingController.setConstants(DrivetrainSettings.headingConstants);

        headingVelocity = (lastPose.getHeading() - localizer.getPose().getHeading()) / headingTimer.seconds();
        headingTimer.reset();
        lastPose = localizer.getPose();

        if(driveMode == DriveMode.AUTONOMOUS){

        }

        leftFront.update();
        rightFront.update();
        leftRear.update();
        rightRear.update();
    }
}
