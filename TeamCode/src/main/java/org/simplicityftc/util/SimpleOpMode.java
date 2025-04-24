package org.simplicityftc.util;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.simplicityftc.commandbase.CommandScheduler;
import org.simplicityftc.electronics.Hub;
import org.simplicityftc.electronics.SimpleVoltageSensor;
import org.simplicityftc.logger.Logger;
import org.simplicityftc.electronics.SimpleGamepad;

public abstract class SimpleOpMode extends LinearOpMode {
    public static CommandScheduler commandScheduler = CommandScheduler.getInstance();
    public static Logger logger = Logger.getInstance();
    private static final ElapsedTime voltageLogTimer = new ElapsedTime();
    private static final ElapsedTime runtime = new ElapsedTime();
    private int loopCount = 0;

    public volatile SimpleGamepad gamepad1 = new SimpleGamepad();
    public volatile SimpleGamepad gamepad2 = new SimpleGamepad();

    public abstract void onInit();
    public void initialize_loop() {}
    public void onStart() {}
    public abstract void run();

    public I2cDeviceSynchSimple getI2cDevice(Hub hub, int bus, int address) {
        return LynxFirmwareVersionManager.createLynxI2cDeviceSynch(
                AppUtil.getDefContext(),
                hub.getLynxModule(),
                bus
        );
    }

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
        logger.add(Logger.LogType.INFO, "OpMode started");

        voltageLogTimer.reset();
        runtime.reset();
        onStart();
        while (opModeIsActive() && !isStopRequested()) {
            run();
            commandScheduler.run();
            Hub.CONTROL_HUB.readData();
            Hub.EXPANSION_HUB.readData();

            if (voltageLogTimer.seconds() >= 0.5) {
                logger.add(Logger.LogType.VOLTAGE, "Current voltage is: " + SimpleVoltageSensor.getVoltage() + " V");
                voltageLogTimer.reset();
            }
            logger.add(Logger.LogType.REFRESH_RATE, "Refresh rate is: " + ++loopCount / runtime.seconds()  + " Hz");
        }
    }
}
