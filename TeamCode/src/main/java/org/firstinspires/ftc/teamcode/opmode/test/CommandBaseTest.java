package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.simplicityftc.commandbase.TimedCommand;

public class CommandBaseTest extends OpMode {
    TimedCommand timedCommand = new TimedCommand(() -> System.out.printf("sigma"), 10);
    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
