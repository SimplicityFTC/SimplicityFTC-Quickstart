package org.simplicityftc.util.math;

public final class SimpleMath{
    public static double normalizeRadians(double radians) {
        radians = (radians + Math.PI) % (Math.PI * 2);
        if (radians < 0) {
            radians += Math.PI;
        }
        radians -= Math.PI;
        return radians;
    }

    public static double normalizeDegrees(double degrees) {
        degrees = (degrees + 180) % 360;
        if (degrees < 0) {
            degrees += 360;
        }
        degrees -= 180;
        return degrees;
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}
