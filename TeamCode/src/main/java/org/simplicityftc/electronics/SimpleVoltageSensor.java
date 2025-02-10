package org.simplicityftc.electronics;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

public class SimpleVoltageSensor {
    private static final double SECONDS_BETWEEN_READS = 0.5;

    private static double lastReadVoltage = -1;
    private static final ElapsedTime timer = new ElapsedTime();

    private SimpleVoltageSensor() { }

    public static double getVoltage() {
        if (timer.seconds() >= SECONDS_BETWEEN_READS || lastReadVoltage < 0) {
            timer.reset();
            lastReadVoltage = Hub.CONTROL_HUB.getLynxModule().getInputVoltage(VoltageUnit.VOLTS);
        }
        return lastReadVoltage;
    }
}
