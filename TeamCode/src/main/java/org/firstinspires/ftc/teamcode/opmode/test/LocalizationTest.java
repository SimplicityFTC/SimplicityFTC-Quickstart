package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.simplicityftc.drivetrain.DrivetrainSettings;
import org.simplicityftc.drivetrain.MecanumDrive;
import org.simplicityftc.drivetrain.follower.Drivetrain;
import org.simplicityftc.util.math.Pose;
import org.simplicityftc.util.math.SimpleMath;

@TeleOp(group = "Teleop Testers")
public class LocalizationTest extends OpMode {
    private Drivetrain drivetrain;

    private final Pose startPose = new Pose(0, 0, 0);

    @Override
    public void init() {
        drivetrain = new MecanumDrive();
        DrivetrainSettings.localizer.setPose(startPose);
    }

    @Override
    public void loop() {
        double forwards = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = SimpleMath.clamp(gamepad1.left_trigger - gamepad1.right_trigger - gamepad1.right_stick_x, -1, 1);

        drivetrain.drive(
                forwards + Math.signum(forwards)*0.15,
                strafe + Math.signum(strafe)*0.15,
                rotate + Math.signum(rotate)*0.15
        );

        telemetry.addData("x (cm)", DrivetrainSettings.localizer.getPose().getX());
        telemetry.addData("y (cm)", DrivetrainSettings.localizer.getPose().getY());
        telemetry.addData("heading (deg)", Math.toDegrees(DrivetrainSettings.localizer.getPose().getHeading()));

        telemetry.update();
        drivetrain.update();
    }

}
