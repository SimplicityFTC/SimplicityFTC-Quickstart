package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.simplicityftc.follower.MecanumDrive;
import org.simplicityftc.util.math.Pose;

@TeleOp(group = "Teleop Testers")
public class LocalizationTest extends OpMode {

    private MecanumDrive mecanumDrive;

    private final Pose startPose = new Pose(0, 0, 0);

    private final boolean ROTATE_USING_TRIGGERS = false;

    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(hardwareMap);
        mecanumDrive.setPosition(startPose);
    }

    @Override
    public void loop() {
        if(ROTATE_USING_TRIGGERS)
            mecanumDrive.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.left_trigger - gamepad1.right_trigger);
        else
            mecanumDrive.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, -gamepad1.right_stick_x);

        telemetry.addData("x (cm)", mecanumDrive.getPosition().getX());
        telemetry.addData("y (cm)", mecanumDrive.getPosition().getY());
        telemetry.addData("heading (deg)", Math.toDegrees(mecanumDrive.getPosition().getHeading()));

        telemetry.update();
        mecanumDrive.update();
    }

}
