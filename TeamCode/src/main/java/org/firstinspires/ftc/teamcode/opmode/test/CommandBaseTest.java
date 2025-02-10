package org.firstinspires.ftc.teamcode.opmode.test;

import org.simplicityftc.commandbase.InstantCommand;
import org.simplicityftc.commandbase.SequentialCommand;
import org.simplicityftc.commandbase.SleepCommand;
import org.simplicityftc.commandbase.TimedCommand;
import org.simplicityftc.commandbase.WaitUntilCommand;
import org.simplicityftc.electronics.SimpleVoltageSensor;
import org.simplicityftc.logger.LogMessage;
import org.simplicityftc.logger.Logger;
import org.simplicityftc.util.SimpleOpMode;

public class CommandBaseTest extends SimpleOpMode {
    TimedCommand timedCommand = new TimedCommand(() -> System.out.printf("sigma"), 10);

    @Override
    public void onInit(){ }

    @Override
    public void initialize_loop(){ }

    @Override
    public void onStart(){
        commandScheduler.schedule(new SequentialCommand(
                new TimedCommand(() -> System.out.printf("whatever"), 10),
                new WaitUntilCommand(() -> getRuntime() > 25),
                timedCommand,
                new InstantCommand(() -> Logger.getInstance().add(new LogMessage().setContent(String.format("%.2f", SimpleVoltageSensor.getVoltage())))),
                new SleepCommand(0.5)
        ));
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException ignore) { }
    }
}
