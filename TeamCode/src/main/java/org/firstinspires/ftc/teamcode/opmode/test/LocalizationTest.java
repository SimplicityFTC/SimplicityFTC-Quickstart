package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.simplicityftc.follower.MecanumDrive;

public class LocalizationTest extends OpMode {
    MecanumDrive mecanumDrive;

    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(hardwareMap);
    }

    @Override
    public void loop() {

    }
}
