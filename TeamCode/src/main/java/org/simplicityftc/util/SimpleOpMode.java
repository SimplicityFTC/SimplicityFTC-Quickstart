package org.simplicityftc.util;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.simplicityftc.commandbase.CommandScheduler;
import org.simplicityftc.devices.Hub;
import org.simplicityftc.devices.SimpleVoltageSensor;
import org.simplicityftc.logger.Logger;
import org.simplicityftc.logicnodes.NodeManager;
import org.simplicityftc.subsystemmanager.SubsystemManager;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleOpMode extends LinearOpMode{
    public enum OpModeType {
        TELE_OP,
        AUTONOMOUS
    }

    public static CommandScheduler commandScheduler = CommandScheduler.getInstance();
    public static Logger logger = Logger.getInstance();
    public static NodeManager nodeManager = NodeManager.getInstance();
    public static SubsystemManager subsystemManager = SubsystemManager.getInstance();

    private static final ElapsedTime voltageLogTimer = new ElapsedTime();
    private static final ElapsedTime runtime = new ElapsedTime();
    public static final ElapsedTime deltaTime = new ElapsedTime();

    public static final List<Runnable> deviceUpdateMethods = new ArrayList<>();

    public OpModeType opModeType = this.getClass().isAnnotationPresent(Autonomous.class) ? OpModeType.AUTONOMOUS : OpModeType.TELE_OP;

    private int loopCount = 0;
    private double loopFrequency;
    private double voltage;

    public abstract void onInit();
    public void initLoop() {}
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

        subsystemManager.clear();

        nodeManager.clear();

        Logger.getInstance().add(Logger.LogType.DEBUG, "number of lynx modules: " + hardwareMap.getAll(LynxModule.class).size());
        Hub.initializeHardware(this.hardwareMap);

        onInit();
        while (!isStarted() && !isStopRequested()) {
            initLoop();
        }
        waitForStart();
        logger.add(Logger.LogType.INFO, "OpMode started");

        voltageLogTimer.reset();
        runtime.reset();
        onStart();
        deltaTime.reset();

        while (opModeIsActive() && !isStopRequested()) {
            // This reads data from the hubs
            Hub.CONTROL_HUB.readData();
            if (Hub.EXPANSION_HUB.getLynxModule() != null) {
                Hub.EXPANSION_HUB.readData();
            }

            // This runs the individual update methods for each device
            deviceUpdateMethods.forEach(Runnable::run);

            // This runs the autonomous nodes logic
            if (opModeType == OpModeType.AUTONOMOUS) {
                nodeManager.run();
            }

            // This is the user's run method
            run();
            deltaTime.reset();

            // This runs the command queue
            commandScheduler.run();

            // THis runs the read, compute, write methods of the subsystems
            subsystemManager.run();

            if (voltageLogTimer.seconds() >= 0.5) {
                voltage = SimpleVoltageSensor.getVoltage();
                logger.add(Logger.LogType.VOLTAGE, "Current voltage is: " + voltage + " V");
                voltageLogTimer.reset();
            }
            loopFrequency = ++loopCount / runtime.seconds();
            logger.add(Logger.LogType.REFRESH_RATE, "Refresh rate is: " + loopFrequency + " Hz");
        }
    }
}
