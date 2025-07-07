package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.simplicityftc.drivetrain.MecanumDrive;
import org.simplicityftc.drivetrain.follower.Drivetrain;
import org.simplicityftc.util.SimpleOpMode;

/**

 frontLeft-----------frontRight
    |                   |
    |                   |
    |                   |
    |                   |
    |                   |
    |                   |
 rearLeft-----------rearRight


 This opmode will sequentially set power to ur motors in a zig zag pattern
 while waiting for 1 second between setting the power
 */

@Autonomous(group = "Default Tuners")
public class MotorWheelDirectionDebugger extends SimpleOpMode {
    Drivetrain drivetrain; //Change to preferred drivetrain
    ElapsedTime timer;
    @Override
    public void onInit() {
        drivetrain = new MecanumDrive();
        drivetrain.setDriveMode(Drivetrain.DriveMode.ROBOT_CENTRIC);
        timer = new ElapsedTime();
    }

    @Override
    public void run() {
        if(timer.seconds() >= 8) {
            timer.reset();
        }

        drivetrain.setMotorPowers( //fix
                (timer.seconds() <= 1) ? 0.7 : 0,
                (Math.abs(timer.seconds() -2.5) <= 0.5) ? 0.7 : 0,
                (Math.abs(timer.seconds() - 4.5) <= 0.5) ? 0.7 : 0,
                (Math.abs(timer.seconds() - 6.5) <= 0.5) ? 0.7 : 0
        );

        drivetrain.update();
    }
}
