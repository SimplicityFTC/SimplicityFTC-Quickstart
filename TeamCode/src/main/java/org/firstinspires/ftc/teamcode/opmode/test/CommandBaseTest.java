package org.firstinspires.ftc.teamcode.opmode.test;

import org.simplicityftc.commandbase.InstantCommand;
import org.simplicityftc.commandbase.SequentialCommand;
import org.simplicityftc.commandbase.TimedCommand;
import org.simplicityftc.commandbase.WaitUntilCommand;
import org.simplicityftc.controlsystem.Motor;
import org.simplicityftc.logger.LogMessage;
import org.simplicityftc.logger.Logger;
import org.simplicityftc.util.SimpleOpMode;

public class CommandBaseTest extends SimpleOpMode {
    TimedCommand timedCommand = new TimedCommand(() -> System.out.printf("sigma"), 10);

    @Override
    public void initialize(){

    }

    @Override
    public void initialize_loop(){

    }

    @Override
    public void start(){
        commandScheduler.schedule(new SequentialCommand(
                new TimedCommand(() -> System.out.printf("sigma"), 10),
                new WaitUntilCommand(() -> getRuntime() > 25),
                new InstantCommand(() -> Logger.getInstance().add(new LogMessage()))
        ));
    }

    @Override
    public void run() {

    }
}
