package org.simplicityftc.util;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SimpleVoltageSensor {
    private static final double SECONDS_BETWEEN_READS = 0.5;

    private static VoltageSensor voltageSensor = null;
    private static double lastReadVoltage = 12;
    private static ElapsedTime timer = null;

    public SimpleVoltageSensor(){
        timer = new ElapsedTime();
    }

    public void init(HardwareMap hardwareMap){
        voltageSensor = hardwareMap.getAll(VoltageSensor.class).iterator().next();
    }

    public static double getVoltage(){
        if (voltageSensor == null)
            Log.println(Log.WARN, "SimpleVoltageSensor", "SENSOR READ WITHOUT BEING INITIALIZED");

        if(timer.seconds() >= SECONDS_BETWEEN_READS){
            timer.reset();
            lastReadVoltage = voltageSensor.getVoltage();
        }

        return lastReadVoltage;
    }
}
