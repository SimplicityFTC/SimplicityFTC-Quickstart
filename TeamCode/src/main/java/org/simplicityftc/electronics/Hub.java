package org.simplicityftc.electronics;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;

import org.simplicityftc.logger.Logger;

public enum Hub {
    CONTROL_HUB(),
    EXPANSION_HUB(),
    SERVO_HUB1(5),
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

}
