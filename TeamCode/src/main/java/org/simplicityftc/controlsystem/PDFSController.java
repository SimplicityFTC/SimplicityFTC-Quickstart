package org.simplicityftc.controlsystem;

public class PDFSController {
    private double kP;
    private double kD;
    private double kF;
    private double kStatic;
    private double lastError;
    private double errorThreshold;
    private double target;

    public PDFSController(double kP, double kD, double kF, double kStatic) { }

    public PDFSController(PDFSConstants pdfsConstants) {
        this.kP = pdfsConstants.getkP();
        this.kD = pdfsConstants.getkD();
        this.kF = pdfsConstants.getkF();
        this.kStatic = pdfsConstants.getkS();
    }

    public void setConstants(PDFSConstants pdfsConstants) {
        this.kP = pdfsConstants.getkP();
        this.kD = pdfsConstants.getkD();
        this.kF = pdfsConstants.getkF();
        this.kStatic = pdfsConstants.getkS();
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double calculate(double currentPosition) {
        return 0;
    }

    public double calculate(double currentPosition, double targetPosition) {
        this.setTarget(targetPosition);
        return calculate(currentPosition);
    }

    private double updateFeedforward(double currentAngle) {
        return 0;
    }
}