package org.simplicityftc.follower;

import org.simplicityftc.controlsystem.PDFSConstants;
import org.simplicityftc.follower.localizer.Localizer;
import org.simplicityftc.follower.localizer.PinpointLocalizer;

//@Config
public class DrivetrainSettings {
    public static final String leftFrontMotorName = "leftFront";
    public static final String rightFrontMotorName = "rightFront";
    public static final String leftRearMotorName = "leftRear";
    public static final String rightRearMotorName = "rightRear";

    public static final boolean reverseLeftFrontMotor = true;
    public static final boolean reverseRightFrontMotor = true;
    public static final boolean reverseLeftRearMotor = true;
    public static final boolean reverseRightRearMotor = true;

    private static final String leftParallelEncoderName = "leftParallelEncoder";
    private static final String perpendicularEncoderName = "perpendicularEncoder";
    private static final String rightParallelEncoderName = "rightParallelEncoder";

    public static final MecanumDrive.DriveMode driveMode = MecanumDrive.DriveMode.ROBOT_CENTRIC;
    public static final boolean headingLock = false;

    public static final Localizer localizer = new PinpointLocalizer();

    public static PDFSConstants xConstants = new PDFSConstants(0, 0, 0, 0);
    public static PDFSConstants yConstants = new PDFSConstants(0, 0, 0, 0);
    public static PDFSConstants headingConstants = new PDFSConstants(0, 0, 0, 0);


}
