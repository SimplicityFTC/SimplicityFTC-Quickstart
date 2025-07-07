package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.simplicityftc.drivetrain.MecanumDrive;
import org.simplicityftc.drivetrain.follower.Drivetrain;
import org.simplicityftc.util.math.Pose;

@Autonomous(group = "Tuners")
public class DrivetrainStaticFeedforwardTuner extends OpMode {

    private Drivetrain drivetrain;
    private double lastSetPower = 0;
    private double iterationsLeft = 10;
    private double kStatic = 0;
    private final double kStaticIncrementPerSecond = 0.15/2.5;
    private ElapsedTime timer;

    @Override
    public void init() {
        drivetrain = new MecanumDrive();

        timer = new ElapsedTime();

        telemetry.addLine("Put the robot on the field with 1 meter of space in front of it");
        telemetry.addLine("Press START to tune static feedforward automatically");
        telemetry.addLine("The tuned static constant will be displayed on telemetry at the end");
        telemetry.update();
    }

    @Override
    public void loop() {
        if(!drivetrain.localizer.getVelocity().equals(new Pose()) && iterationsLeft != 0) {
            kStatic += lastSetPower;

            iterationsLeft -= 1;
            lastSetPower = 0;

            if(iterationsLeft == 0) {
                telemetry.addData("Tuned KSTATIC", kStatic / 10);
                telemetry.update();
            }
        }

        lastSetPower += timer.seconds() * kStaticIncrementPerSecond;

        if(iterationsLeft != 0) {
            drivetrain.setMotorPowers( lastSetPower * Math.signum((iterationsLeft + 1) % 2 - 0.5),
                                        lastSetPower * Math.signum((iterationsLeft + 1) % 2 - 0.5),
                                         lastSetPower * Math.signum((iterationsLeft + 1) % 2 - 0.5),
                                        lastSetPower * Math.signum((iterationsLeft + 1) % 2 - 0.5));
        } else {
            drivetrain.setMotorPowers(0, 0, 0, 0);
            try {
                wait(1000); //ik, ew
            } catch (InterruptedException ignored) { }
        }

        drivetrain.update();
    }
}
