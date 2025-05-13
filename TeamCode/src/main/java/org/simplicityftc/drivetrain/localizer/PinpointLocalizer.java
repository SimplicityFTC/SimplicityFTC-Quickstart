package org.simplicityftc.drivetrain.localizer;

import static org.simplicityftc.drivetrain.localizer.GoBildaPinpointDriver.EncoderDirection.FORWARD;
import static org.simplicityftc.drivetrain.localizer.GoBildaPinpointDriver.EncoderDirection.REVERSED;

import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.simplicityftc.electronics.Hub;
import org.simplicityftc.util.math.Pose;

public class PinpointLocalizer implements Localizer {
    private final static int bus = 0;

    private final static double xOffset = -84;
    private final static double yOffset = -168;
    private final static boolean forwardEncoderReversed = false;
    private final static boolean strafeEncoderReversed = false;

    private final GoBildaPinpointDriver pinpoint;

    public PinpointLocalizer() {
        if (bus < 0 || bus > 3) throw new IllegalArgumentException("Port must be between 0 and 3");
        pinpoint = new GoBildaPinpointDriver(
               LynxFirmwareVersionManager.createLynxI2cDeviceSynch(
                       AppUtil.getDefContext(),
                       Hub.CONTROL_HUB.getLynxModule(),
                       bus
               ),
               true
        );
        pinpoint.setOffsets(xOffset, yOffset);
        pinpoint.setEncoderDirections(
                (forwardEncoderReversed ? REVERSED : FORWARD),
                (strafeEncoderReversed ? REVERSED : FORWARD)
        );
        pinpoint.recalibrateIMU();
        pinpoint.resetPosAndIMU();
    }

    @Override
    public Pose getPose() {
        return pinpoint.getPosition();
    }

    @Override
    public Pose getVelocity() {
        return pinpoint.getVelocity();
    }

    @Override
    public void setPose(Pose pose) {
        pinpoint.setPosition(pose);
    }

    @Override
    public void update() {
        pinpoint.update();
    }
}
