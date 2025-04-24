package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.simplicityftc.electronics.Hub;
import org.simplicityftc.electronics.SimpleI2CDevice;
import org.simplicityftc.util.SimpleOpMode;

public class I2CDeviceTest extends SimpleOpMode {

    DistanceSensor sensorDistance;

    int bus = 0;
    int address = 0x40;

    @Override
    public void onInit() {
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_distance");

        //TODO: Marius gotta change ts 🥀
        ColorSensor colorSensor = (ColorSensor) getI2cDevice(Hub.CONTROL_HUB, bus, address);
        DistanceSensor distanceSensor = (DistanceSensor) getI2cDevice(Hub.CONTROL_HUB, bus, address);

        IMU imu = (IMU) getI2cDevice(Hub.CONTROL_HUB, bus, address);
        //check if ts work
    }

    @Override
    public void run() {

    }
}
