package org.simplicityftc.util.math;

public final class SimpleMath{
    //normalization using the formula found in goBilda's pinpoint driver code
    public static double normalizeRadians(double radians) {
        return ((radians + Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI) - Math.PI;
    }

    public static double normalizeDegrees(double degrees) {
        return ((degrees + 180) % (2 * 180) + 2 * 180) % (2 * 180) - 180;
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}
