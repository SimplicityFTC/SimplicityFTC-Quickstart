package org.simplicityftc.electronics;

import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetServoEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoConfigurationCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoPulseWidthCommand;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.util.Range;

import org.simplicityftc.logger.Logger;
import org.simplicityftc.util.math.SimpleMath;


public class SimpleServo {
    private final int port;
    private final Hub hub;

    private double lastPosition = 0;
    private double positionSetTolerance = 0.03;

    private int lowerPWM = 500;
    private int upperPWM = 2500;

    private boolean shouldUpdate = true;

    public SimpleServo(Hub hub, int port) {
        if(port < 0 || port > 5) throw new IllegalArgumentException("Port must be between 0 and 5");
        this.hub = hub;
        this.port = port;

        try {
            new LynxSetServoConfigurationCommand(hub.getLynxModule(), port, (int)new PwmControl.PwmRange(500, 2500).usFrame);
            while(!new LynxGetServoEnableCommand(hub.getLynxModule(), port).sendReceive().isEnabled()) {
                new LynxSetServoEnableCommand(hub.getLynxModule(), port, true).send();
            }
        } catch (InterruptedException | LynxNackException exception) {
            Logger.getInstance().add(Logger.LogType.ERROR, exception.getMessage());
        }
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
     * @param position value in range (-1, 1)
     */
    public void setPosition(double position) {
        position = Math.min(Math.max(position, 0.0), 1.0);

        if(Math.abs(position - lastPosition) < positionSetTolerance)
            return;

        lastPosition = position;
        shouldUpdate = true;
    }

    public double getTargetPosition() {
        return lastPosition;
    }

    public void setTolerance(double tolerance) {
        tolerance = SimpleMath.clamp(tolerance, 0, 0.05);
        this.positionSetTolerance = tolerance;
    }

    public void update() {
        if (!shouldUpdate) {
            return;
        }

        int pwm = (int)Range.scale(lastPosition, 0.0, 1.0, lowerPWM, upperPWM);

        try {
            while(!new LynxGetServoEnableCommand(hub.getLynxModule(), port).sendReceive().isEnabled())
                new LynxSetServoEnableCommand(hub.getLynxModule(), port, true).send();
            new LynxSetServoPulseWidthCommand(
                    hub.getLynxModule(),
                    port,
                    pwm
            ).send();
        }
        catch (InterruptedException | LynxNackException exception) {
            Logger.getInstance().add(Logger.LogType.ERROR, exception.getMessage());
        }
    }
}
