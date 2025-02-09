package org.simplicityftc.util;

import com.qualcomm.hardware.lynx.LynxDcMotorController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.simplicityftc.commandbase.CommandScheduler;
import org.simplicityftc.electronics.Hub;

public abstract class SimpleOpMode extends OpMode {
    public static CommandScheduler commandScheduler;

    public void initialize() { }

    public void initialize_loop() { }

    public void run() { }

    @Override
    public final void init() {
        for(LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            if(module.isParent() && LynxConstants.isEmbeddedSerialNumber(module.getSerialNumber())) {
                Hub.CONTROL_HUB.setHub(module);
            } else {
                Hub.EXPANSION_HUB.setHub(module);
            }
        }

        commandScheduler = CommandScheduler.getInstance();

        initialize();
    }

    @Override
    public final void init_loop() {
        initialize_loop();
    }

    @Override
    public final void loop() {
        run();
        CommandScheduler.getInstance().run();
        Hub.CONTROL_HUB.getLynxModule().clearBulkCache();
        Hub.EXPANSION_HUB.getLynxModule().clearBulkCache();
        //TODO: Log update rate
    }
}
