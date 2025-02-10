package org.simplicityftc.util;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import org.simplicityftc.commandbase.CommandScheduler;
import org.simplicityftc.electronics.Hub;

public abstract class SimpleOpMode extends LinearOpMode {
    public static CommandScheduler commandScheduler = CommandScheduler.getInstance();

    public void onInit() { }

    public void initialize_loop() { }

    public void onStart() { }

    public void run() { }

    @Override
    public void runOpMode() throws InterruptedException {
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            if(module.isParent() && LynxConstants.isEmbeddedSerialNumber(module.getSerialNumber())) {
                Hub.CONTROL_HUB.setHub(module);
            } else {
                Hub.EXPANSION_HUB.setHub(module);
            }
        }

        commandScheduler.reset();

        onInit();

        while (!isStarted() && !isStopRequested()) {
            initialize_loop();
        }

        waitForStart();
        onStart();

        while (opModeIsActive() && !isStopRequested()) {
            run();
            CommandScheduler.getInstance().run();
            Hub.CONTROL_HUB.readData();
            Hub.EXPANSION_HUB.readData();
            //TODO: Log update rate
        }
    }
}
