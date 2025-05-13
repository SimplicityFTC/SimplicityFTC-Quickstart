package org.simplicityftc.drivetrain;

import com.acmerobotics.dashboard.config.Config;

import org.simplicityftc.controlsystem.PDFSConstants;
import org.simplicityftc.drivetrain.follower.Drivetrain;
import org.simplicityftc.electronics.Hub;
import org.simplicityftc.drivetrain.localizer.Localizer;
import org.simplicityftc.drivetrain.localizer.PinpointLocalizer;

@Config
public class DrivetrainSettings {
    public static final Hub DRIVETRAIN_MOTORS_HUB = Hub.CONTROL_HUB;
    public static final int leftFrontMotorPort = 0;
    public static final int rightFrontMotorPort = 1;
    public static final int leftRearMotorPort = 2;
    public static final int rightRearMotorPort = 3;

    public static final boolean reverseLeftFrontMotor = false;
    public static final boolean reverseRightFrontMotor = true;
    public static final boolean reverseLeftRearMotor = false;
    public static final boolean reverseRightRearMotor = true;

    public static final Hub ODOMETRY_ENCODERS_HUB = Hub.CONTROL_HUB;
    private static final int leftParallelEncoderPort = 0;
    private static final int perpendicularEncoderPort = 1;
    private static final int rightParallelEncoderPort = 2;

    public static final Drivetrain.DriveMode driveMode = Drivetrain.DriveMode.ROBOT_CENTRIC;
    public static final boolean headingLock = false;

    public static final boolean coastInTeleop = false;

    public static final Localizer localizer = new PinpointLocalizer();

    public static double followerTolerance = 2.5;//cm before atTarget() returns true
    public static PDFSConstants forwardConstants = new PDFSConstants(0, 0, 0, 0);
    public static PDFSConstants strafeConstants = new PDFSConstants(0, 0, 0, 0);
    public static PDFSConstants headingConstants = new PDFSConstants(0, 0, 0, 0);
    public static double K_STATIC = 0;

    public static double translationalMaxPower = 1;
    public static double rotationalMaxPower = 1;
}
