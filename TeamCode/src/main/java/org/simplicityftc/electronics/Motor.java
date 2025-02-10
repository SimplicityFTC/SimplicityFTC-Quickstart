package org.simplicityftc.electronics;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Motor {
    private final int port;
    private final Hub hub;

    private boolean isReversed = false;
    private double lastPower = -1e7;
    private double lastPosition;
    private double lastVelocity;
    private final double powerSetTolerance = 0.03;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean shouldUpdate = true;

    public Motor(Hub hub, int port) {
        if(port < 0 || port > 3) throw new IllegalArgumentException("Port must be between 0 and 3");
        this.hub = hub;
        this.port = port;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    public void setPower(double power) {
        power = Math.min(Math.max(power, -1), 1);

        if(Math.abs(power - lastPower) < powerSetTolerance && power != 1 && power != -1 && power != 0)
            return;

        lastPower = isReversed?-power:power;
        shouldUpdate = true;
    }

    /**
     * @param coasting
     * true if motor should passively coast when power is 0
     * false if motor should passively brake when power is 0
     */
    public void setCoasting(boolean coasting) {
        hub.getMotorController().setMotorZeroPowerBehavior(port, coasting ? DcMotor.ZeroPowerBehavior.FLOAT : DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * @return motor position in ticks
     */
    public double getPosition() {
        return lastPosition;
    }

    /**
     * @return motor velocity in ticks/s
     */
    public double getVelocity() {
        return lastVelocity;
    }

    /**
     * @return true if motor is over current
     */
    public boolean isOverCurrent() {
        return hub.getMotorController().isMotorOverCurrent(port);
    }

    /**
     * @param amps over current threshold in amps
     */
    public void setOverCurrentThreshold(double amps) {
       hub.getMotorController().setMotorCurrentAlert(port, amps, CurrentUnit.AMPS);
    }

    public void update() {
        double newPosition = hub.getMotorController().getMotorCurrentPosition(port);
        lastVelocity = (newPosition - lastPosition) / timer.seconds();
        timer.reset();
        lastPosition = newPosition;

        if(!shouldUpdate) {
            return;
        }

        hub.getMotorController().setMotorPower(port, lastPower);
    }
}
