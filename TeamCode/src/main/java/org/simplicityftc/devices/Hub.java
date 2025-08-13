package org.simplicityftc.devices;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoConfigurationCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoEnableCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import org.simplicityftc.logger.Logger;

public enum Hub {
    CONTROL_HUB(),
    EXPANSION_HUB(),
    SERVO_HUB1(3),
    SERVO_HUB2(),
    SERVO_HUB3(),
    SERVO_HUB4();

    public final int canId;

    Hub() {
        this.canId = 0;
    }

    Hub(int canId) {
        if(canId < 1 || canId > 10) throw new IllegalArgumentException("Port must be between 1 and 10");
        this.canId = canId;
    }

    private LynxModule lynxModule;
    private LynxGetBulkInputDataResponse bulkData;

    public void setHub(LynxModule lynxModule) {
        if (this.lynxModule != null) throw new RuntimeException("Attempt to change Lynx module of initialized Hub");

        this.lynxModule = lynxModule;

        lynxModule.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
    }

    public void readData() {
        if (this.lynxModule == null) throw new RuntimeException("Attempt to read data from null Lynx module");

        this.lynxModule.clearBulkCache();

        try {
            bulkData = new LynxGetBulkInputDataCommand(this.lynxModule).sendReceive();
        } catch (InterruptedException | LynxNackException exception) {
            Logger.getInstance().add(Logger.LogType.ERROR, exception.getMessage());
        }
    }

    public LynxModule getLynxModule() {
        return this.lynxModule;
    }

    public LynxGetBulkInputDataResponse getBulkData() {
        return bulkData;
    }

    public static void initializeHardware(HardwareMap hardwareMap) {
        Logger.getInstance().add(Logger.LogType.DEBUG, "allahu akubaru");
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            Logger.getInstance().add(Logger.LogType.DEBUG, "Found LynxModule: " + module.getDeviceName());
            if (module.isParent() && LynxConstants.isEmbeddedSerialNumber(module.getSerialNumber()) && Hub.CONTROL_HUB.getLynxModule() == null) {
                Hub.CONTROL_HUB.setHub(module);
            } else {
                if (module.getDeviceName().contains("Servo Hub")) {
                    Logger.getInstance().add(Logger.LogType.DEBUG, "skibidee");
                    Logger.getInstance().add(Logger.LogType.DEBUG, module.getDeviceName());
                    for (Hub hub : Hub.values()) {
                        if (hub.canId == 0 || hub.getLynxModule() != null) continue;
                        if (module.getDeviceName().endsWith("" + hub.canId)) {
                            hub.setHub(module);
                            break;
                        }
                    }
                } else if (Hub.EXPANSION_HUB.getLynxModule() == null) {
                    {
                        Hub.EXPANSION_HUB.setHub(module);
                    }
                }
            }
            for (int i = 0; i < 6; i += 1) {
                try {
                } catch (InterruptedException | LynxNackException ignored) { }
            }
        }
    }
}
