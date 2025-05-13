package org.simplicityftc.drivetrain.follower;



public interface Drivetrain {
    enum DriveMode {
        FIELD_CENTRIC,
        ROBOT_CENTRIC,
        AUTONOMOUS
    }
    void setMotorPowers(double leftFront, double rightFront, double leftRear, double rightRear);
    void setDriveMode(Drivetrain.DriveMode driveMode);
    void drive(double forwards, double strafe, double turn);
    void update();
}
