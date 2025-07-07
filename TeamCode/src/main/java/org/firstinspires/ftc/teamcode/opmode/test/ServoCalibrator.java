package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.hardware.lynx.commands.core.LynxGetServoConfigurationCommand;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.simplicityftc.electronics.Hub;
import org.simplicityftc.electronics.SimpleServo;
import org.simplicityftc.util.SimpleOpMode;

@TeleOp(group = "Tuners")
public class ServoCalibrator extends SimpleOpMode {

    SimpleServo servo;

    @Override
    public void onInit() {
        servo = new SimpleServo(Hub.CONTROL_HUB, 0);
        servo.setTolerance(0);
        servo.setPosition(0.5);
        servo.update();
        System.out.println("default frameperiod" + new LynxGetServoConfigurationCommand(Hub.CONTROL_HUB.getLynxModule(), hardwareMap.get(Servo.class, "skbidee").getPortNumber()).toPayloadByteArray()[0]);
    }

    @Override
    public void run() {
        servo.setPosition(servo.getTargetPosition() + (gamepad1.right_trigger - gamepad1.left_trigger)*deltaTime.seconds());

        servo.update();
        telemetry.addLine(String.format("Servo Position: %.3f", servo.getTargetPosition()));
        telemetry.addLine(String.format("gamepad1 a %b, right trigger %.3f", gamepad1.a, (gamepad1.right_trigger - gamepad1.left_trigger)*deltaTime.seconds()));
        telemetry.update();
    }
}
