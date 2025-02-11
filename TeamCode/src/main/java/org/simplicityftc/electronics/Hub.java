package org.simplicityftc.electronics;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;

public enum Hub {
    CONTROL_HUB,
    EXPANSION_HUB;

    private LynxModule lynxModule;
    private LynxGetBulkInputDataResponse bulkData;

    public void setHub(LynxModule lynxModule) {
        this.lynxModule = lynxModule;

        lynxModule.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
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

}
