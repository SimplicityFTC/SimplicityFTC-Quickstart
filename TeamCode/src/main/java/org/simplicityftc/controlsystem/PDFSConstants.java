package org.simplicityftc.controlsystem;

public class PDFSConstants {
    private final double kP;
    private final double kD;
    private final double kF;
    private final double kS;

    public PDFSConstants(double kP, double kD, double kF, double kS) {
        this.kP = kP;
        this.kD = kD;
        this.kF = kF;
        this.kS = kS;
    }

    public double getkP() {
        return kP;
    }

    public double getkD() {
        return kD;
    }

    public double getkF() {
        return kF;
    }

    public double getkS() {
         return kS;
    }
}
