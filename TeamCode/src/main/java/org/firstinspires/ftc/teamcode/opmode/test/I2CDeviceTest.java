package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.simplicityftc.electronics.Hub;
import org.simplicityftc.util.SimpleOpMode;

@TeleOp(group = "Debug")
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
