package org.simplicityftc.electronics;

import com.qualcomm.hardware.lynx.LynxDcMotorController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.robotcore.exception.RobotCoreException;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public enum Hub {
    CONTROL_HUB,
    EXPANSION_HUB;

    private LynxModule lynxModule;
    private LynxDcMotorController lynxDcMotorController;
    private LynxServoController lynxServoController;
    private LynxGetBulkInputDataResponse bulkData;

    public void setHub(LynxModule lynxModule) {
        this.lynxModule = lynxModule;

        lynxModule.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);

        try {
            this.lynxDcMotorController = new LynxDcMotorController(AppUtil.getDefContext(), lynxModule);
        } catch (RobotCoreException | InterruptedException ignored) { }

        try{
            this.lynxServoController = new LynxServoController(AppUtil.getDefContext(), lynxModule);
        } catch (RobotCoreException | InterruptedException ignored) { }
    }

    public void readData() {
        lynxModule.clearBulkCache();
        try {
            bulkData = new LynxGetBulkInputDataCommand(lynxModule).sendReceive();
        } catch (InterruptedException |LynxNackException ignored) { }
    }

    public LynxModule getLynxModule() {
        return lynxModule;
    }

    public LynxGetBulkInputDataResponse getBulkData() {
        return bulkData;
    }

    public LynxDcMotorController getMotorController() {
        return lynxDcMotorController;
    }

    public LynxServoController getLynxServoController() {
        return lynxServoController;
    }

}
