package org.simplicityftc.electronics;

import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorChannelCurrentAlertLevelCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorChannelModeCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorConstantPowerCommand;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SimpleMotor {
    private final int port;
    private final Hub hub;

    private boolean isReversed = false;
    private double lastPower = -1e7;
    private double positionOffset = 0;
    private double lastPosition = 0;
    private double lastVelocity;
    private final double powerSetTolerance = 0.03;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean shouldUpdate = true;

    public SimpleMotor(Hub hub, int port) {
        if (port < 0 || port > 3) throw new IllegalArgumentException("Port must be between 0 and 3");
        this.hub = hub;
        this.port = port;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    /**
     * @param power value in range (-1, 1)
     */
    public void setPower(double power) {
        power = Math.min(Math.max(power, -1), 1);

        if (Math.abs(power - lastPower) < powerSetTolerance && power != 1 && power != -1 && power != 0) {
            return;
        }

        lastPower = isReversed?-power:power;
        shouldUpdate = true;
    }

    /**
     * @param coasting
     * true if motor should passively coast when power is 0
     * false if motor should passively brake when power is 0
     */
    public void setCoasting(boolean coasting) {
        try {
            new LynxSetMotorChannelModeCommand(
                    hub.getLynxModule(),
                    port,
                    DcMotor.RunMode.RUN_WITHOUT_ENCODER,
                    coasting ? DcMotor.ZeroPowerBehavior.FLOAT : DcMotor.ZeroPowerBehavior.BRAKE
            ).send();
        } catch (InterruptedException | LynxNackException ignored) { }
    }

    /**
     * @param newPosition new position in ticks
     */
    public void setPosition(double newPosition) {
        newPosition *= (isReversed ? -1 : 1);
        positionOffset = lastPosition + positionOffset - newPosition;
    }

    /**
     * @return motor position in ticks
     */
    public double getPosition() {
        return (hub.getBulkData().getEncoder(port) + positionOffset) * (isReversed ? -1 : 1);
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
        return hub.getBulkData().isOverCurrent(port);
    }

    /**
     * @param amps over current threshold in amps
     */
    public void setOverCurrentThreshold(double amps) {
        try {
            new LynxSetMotorChannelCurrentAlertLevelCommand(
                    hub.getLynxModule(),
                    port,
                    (int)(amps * 1000.0)
            ).send();
        } catch (InterruptedException | LynxNackException ignored) { }
    }

    public void update() {
        double newPosition = getPosition();
        lastVelocity = (newPosition - lastPosition) / timer.seconds();
        timer.reset();
        lastPosition = newPosition;

        if (!shouldUpdate) {
            return;
        }

        try {
            new LynxSetMotorConstantPowerCommand(
                    hub.getLynxModule(),
                    port,
                    (int)(lastPower * 32767)
            ).send();
        }
        catch (InterruptedException | RuntimeException | LynxNackException ignored) { }
    }
}
