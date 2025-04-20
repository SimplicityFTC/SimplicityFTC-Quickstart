package org.simplicityftc.controlsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.simplicityftc.util.math.SimpleMath;

public class PDFSController {
    private double kP;
    private double kD;
    private double max_kF;
    private double min_kF;
    private double kStatic;
    private double feedforwardMultiplier = 1;

    private double lastPosition;
    private double errorThreshold;
    private double target;

    private ElapsedTime timer;

    public PDFSController(PDFSConstants pdfsConstants) {
        this(pdfsConstants.getkP(), pdfsConstants.getkD(), pdfsConstants.getkF(), pdfsConstants.getkS());
    }

    public PDFSController(double kP, double kD, double kF, double kStatic) {
        setConstants(kP, kD, kF, kStatic);
        timer = new ElapsedTime();
    }

    public void setConstants(PDFSConstants pdfsConstants) {
        setConstants(pdfsConstants.getkP(), pdfsConstants.getkD(), pdfsConstants.getkF(), pdfsConstants.getkS());
    }

    public void setConstants(double kP, double kD, double kF, double kStatic) {
        this.kP = kP;
        this.kD = kD;
        this.min_kF = kF;
        this.kStatic = kStatic;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double calculate(double currentPosition, double targetPosition) {
        this.setTarget(targetPosition);
        return calculate(currentPosition);
    }

    public double calculate(double currentPosition) {

        double error = target - currentPosition;
        double currentVelocity = (lastPosition - currentPosition) / timer.seconds();
        lastPosition = currentPosition;

        double feedforwardPower = feedforwardMultiplier * min_kF;
        double staticPower = (Math.abs(error) < errorThreshold) ? Math.signum(error) * kStatic : 0;
        double output = kP * error - kD * currentVelocity + staticPower + feedforwardPower;


        return output;
    }


    /**
     * @param multiplier value between 0 and 1
     * Example: consider a pivoting arm, you would want the feedforward
     * to scale sinusoidally with the arm's angle from the ground:
     * for the pivot motor:

     update(){
       //...
       controller.setFeedforwardMultiplier(sin(angle));
       power = controller.calculate(motor.getPosition());
       //...
     }

     */

    private void setFeedforwardMultiplier(double multiplier){
        feedforwardMultiplier = SimpleMath.clamp(multiplier, 0, 1);
    }
}