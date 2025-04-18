package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.simplicityftc.follower.MecanumDrive;
import org.simplicityftc.util.math.Pose;
import org.simplicityftc.util.math.SimpleMath;

@TeleOp(group = "Teleop Testers")
public class LocalizationTest extends OpMode {
    private MecanumDrive mecanumDrive;

    private final Pose startPose = new Pose(0, 0, 0);

    @Override
    public void init() {
        mecanumDrive = new MecanumDrive();
        mecanumDrive.setPosition(startPose);
    }

    @Override
    public void loop() {
        double forwards = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = SimpleMath.clamp(gamepad1.left_trigger - gamepad1.right_trigger - gamepad1.right_stick_x, -1, 1);

        mecanumDrive.drive(
                forwards + Math.signum(forwards)*0.15,
                strafe + Math.signum(strafe)*0.15,
                rotate + Math.signum(rotate)*0.15
        );

        telemetry.addData("x (cm)", mecanumDrive.getPosition().getX());
        telemetry.addData("y (cm)", mecanumDrive.getPosition().getY());
        telemetry.addData("heading (deg)", Math.toDegrees(mecanumDrive.getPosition().getHeading()));

        telemetry.update();
        mecanumDrive.update();
    }

}
