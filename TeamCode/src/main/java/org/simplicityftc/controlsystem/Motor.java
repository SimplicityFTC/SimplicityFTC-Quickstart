package org.simplicityftc.controlsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Motor{

    private final DcMotorEx motor;

    private boolean isReversed = false;
    private double lastPower = -1e7;
    private double lastPosition;
    private double lastVelocity;
    private final double powerSetTolerance = 0.03;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean shouldUpdate = true;

    public Motor(DcMotorSimple dcMotorSimple){
        this.motor = (DcMotorEx) dcMotorSimple;
    }
    public Motor(DcMotor dcMotorSimple){
        this.motor = (DcMotorEx) dcMotorSimple;
    }
    public Motor(DcMotorEx dcMotorSimple){
        this.motor = dcMotorSimple;
    }

    public void setReversed(boolean reversed){
        isReversed = reversed;
    }

    public void setPower(double power){
        power = Math.min(Math.max(power, -1), 1);

        if(Math.abs(power - lastPower) < powerSetTolerance && power != 1 && power != -1 && power != 0)
            return;

        lastPower = isReversed?-power:power;
        shouldUpdate = true;
    }

    public void setCoasting(boolean coasting){
        motor.setZeroPowerBehavior(coasting?DcMotor.ZeroPowerBehavior.FLOAT:DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public double getPosition(){
        return motor.getCurrentPosition();
    }

    public double getVelocity(){
        return lastVelocity;
    }

    public void update(){
        lastVelocity = (motor.getCurrentPosition() - lastPosition) / timer.seconds();
        timer.reset();
        lastPosition = motor.getCurrentPosition();

        if(!shouldUpdate)
            return;

        motor.setPower(lastPower);
    }
}
