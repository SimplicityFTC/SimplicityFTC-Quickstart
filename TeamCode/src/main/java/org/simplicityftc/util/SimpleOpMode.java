package org.simplicityftc.util;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoConfigurationCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoEnableCommand;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.simplicityftc.commandbase.CommandScheduler;
import org.simplicityftc.devices.Hub;
import org.simplicityftc.devices.SimpleVoltageSensor;
import org.simplicityftc.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleOpMode extends LinearOpMode{
    public static CommandScheduler commandScheduler = CommandScheduler.getInstance();
    public static Logger logger = Logger.getInstance();
    private static final ElapsedTime voltageLogTimer = new ElapsedTime();
    private static final ElapsedTime runtime = new ElapsedTime();
    public static final ElapsedTime deltaTime = new ElapsedTime();
    public static final List<Runnable> deviceUpdateMethods = new ArrayList<>();
    private int loopCount = 0;

    public abstract void onInit();
    public void initialize_loop() {}
    public void onStart() {}
    public abstract void run();

    public I2cDeviceSynchSimple getI2cDevice(Hub hub, int bus, int address) {
        I2cDeviceSynchSimple i2cDevice = LynxFirmwareVersionManager.createLynxI2cDeviceSynch(
                AppUtil.getDefContext(),
                hub.getLynxModule(),
                bus);
        i2cDevice.setI2cAddress(I2cAddr.create7bit(address));
        return i2cDevice;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        deviceUpdateMethods.clear();

        commandScheduler.reset();

        Logger.getInstance().add(Logger.LogType.DEBUG,String.format("number of lynx modules: %n", hardwareMap.getAll(LynxModule.class).size()));
        Hub.initializeHardware(this.hardwareMap);

        onInit();
        while (!isStarted() && !isStopRequested()) {
            initialize_loop();
        }
        waitForStart();
        logger.add(Logger.LogType.INFO, "OpMode started");

        voltageLogTimer.reset();
        runtime.reset();
        onStart();
        deltaTime.reset();
        while (opModeIsActive() && !isStopRequested()) {
            Hub.CONTROL_HUB.readData();
            if (Hub.EXPANSION_HUB.getLynxModule() != null) {
                Hub.EXPANSION_HUB.readData();
            }
            deviceUpdateMethods.forEach(Runnable::run);
            run();
            deltaTime.reset();
            commandScheduler.run();

            if (voltageLogTimer.seconds() >= 0.5) {
                logger.add(Logger.LogType.VOLTAGE, "Current voltage is: " + SimpleVoltageSensor.getVoltage() + " V");
                voltageLogTimer.reset();
            }
            logger.add(Logger.LogType.REFRESH_RATE, "Refresh rate is: " + ++loopCount / runtime.seconds()  + " Hz");
        }
    }
}
