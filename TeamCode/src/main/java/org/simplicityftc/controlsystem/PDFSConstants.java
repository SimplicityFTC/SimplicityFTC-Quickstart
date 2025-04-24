package org.simplicityftc.controlsystem;

public class PDFSConstants {
    private final double kP;
    private final double kD;
    private final double min_kF;
    private final double max_kF;
    private final double kS;

    public PDFSConstants(double kP, double kD, double min_kF, double max_kF, double kS) {
        this.kP = kP;
        this.kD = kD;
        this.min_kF = min_kF;
        this.max_kF = max_kF;
        this.kS = kS;
    }

    public PDFSConstants(double kP, double kD, double kF, double kS) {
        this(kP, kD, kF, kF, kS);
    }

    public double getkP() {
        return kP;
    }

    public double getkD() {
        return kD;
    }

    public double getmin_kF() {
        return min_kF;
    }

    public double getmax_kF() {
        return max_kF;
    }

    public double getkS() {
         return kS;
    }
}
