package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.simplicityftc.follower.MecanumDrive;

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

public class MotorWheelDirectionDebugger extends OpMode {
    MecanumDrive mecanumDrive;
    ElapsedTime timer;
    @Override
    public void init() {
        mecanumDrive = new MecanumDrive();
        mecanumDrive.setDriveMode(MecanumDrive.DriveMode.ROBOT_CENTRIC);
        timer = new ElapsedTime();
    }

    @Override
    public void loop() {
        if(timer.seconds() >= 8) {
            timer.reset();
        }

        mecanumDrive.setMotorPowers(
                (timer.seconds() <= 1) ? 0.7 : 0,
                (Math.abs(timer.seconds() -2.5) <= 0.5) ? 0.7 : 0,
                (Math.abs(timer.seconds() - 4.5) <= 0.5) ? 0.7 : 0,
                (Math.abs(timer.seconds() - 6.5) <= 0.5) ? 0.7 : 0
        );

        mecanumDrive.update();
    }
}
