package org.simplicityftc.drivetrain.localizer;


import static com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection.FORWARD;
import static com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection.REVERSED;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.simplicityftc.devices.Hub;
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
        pinpoint.setOffsets(xOffset, yOffset, DistanceUnit.CM);
        pinpoint.setEncoderDirections(
                (forwardEncoderReversed ? REVERSED : FORWARD),
                (strafeEncoderReversed ? REVERSED : FORWARD)
        );
        pinpoint.recalibrateIMU();
        pinpoint.resetPosAndIMU();
    }

    @Override
    public Pose getPose() {
        return (Pose)pinpoint.getPosition();
    }

    @Override
    public Pose getVelocity() {
        //should we use heading velocity? nah i'm sigma
        return new Pose(pinpoint.getVelX(DistanceUnit.CM), pinpoint.getVelY(DistanceUnit.CM), 0);
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
