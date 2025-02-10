package org.simplicityftc.electronics;

import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoPulseWidthCommand;
import com.qualcomm.robotcore.util.Range;


public class SimpleCRServo {
    private final int port;
    private final Hub hub;

    private double lastPower = 0;
    private final double powerSetTolerance = 0.03;

    private int lowerPWM = 500;
    private int upperPWM = 2500;

    private boolean shouldUpdate = true;

    public SimpleCRServo(Hub hub, int port) {
        if(port < 0 || port > 5) throw new IllegalArgumentException("Port must be between 0 and 5");
        this.hub = hub;
        this.port = port;
    }

    /**
     * @param lowerPWM value in us, default is 500us
     * @param upperPWM value in us, default is 2500us
     */
    public void setPWMRange(int lowerPWM, int upperPWM) {
        this.lowerPWM = lowerPWM;
        this.upperPWM = upperPWM;
    }

    /**
     * @param power value in range (-1.0, 1.0)
     */
    public void setPower(double power) {
        power = Math.min(Math.max(power, -1), 1);

        if (Math.abs(power - lastPower) < powerSetTolerance && power != 1 && power != -1 && power != 0) {
            return;
        }

        lastPower = power;
        shouldUpdate = true;
    }

    public double getPower() {
        return lastPower;
    }

    public void update() {

        if(!shouldUpdate) {
            return;
        }

        int pwm = (int)Range.scale(lastPower, -1, 1, lowerPWM, upperPWM);

        try {
            new LynxSetServoPulseWidthCommand(
                    hub.getLynxModule(),
                    port,
                    pwm
            ).send();
        }
        catch (InterruptedException | RuntimeException | LynxNackException ignored) { }
    }
}
