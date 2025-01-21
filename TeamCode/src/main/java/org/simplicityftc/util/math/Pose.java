package org.simplicityftc.util.math;

import androidx.annotation.NonNull;

public class Pose {
    private double x;
    private double y;
    private double heading;

    /**
     * @param x x coordinate of position vector
     * @param y y coordinate of position vector
     * @param heading rotation of position vector
     */
    public Pose(double x, double y, double heading){
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    /**
     * @param x x coordinate of position vector
     * @param y y coordinate of position vector
     */
    public Pose(double x, double y){
        this(x, y, 0);
    }

    /**
     * Returns a pose with coordinates (0, 0) and heading [0]
     */
    public Pose(){
        this(0, 0);
    }

    /**
     * @param pose pose to add to the old one
     * @return a new Pose with the position vector added
     */
    public Pose add(@NonNull Pose pose){
        return new Pose(this.x + pose.x, this.y + pose.y, this.heading + pose.heading);
    }

    /**
     * @param scalar scalar to multiply the positional vector with
     * @return a new Pose with the position vector scaled
     */
    public Pose scale(double scalar) {
        return new Pose(this.x * scalar, this.y * scalar, this.heading);
    }

    /**
     * @param pose pose to subtract from the old one
     * @return a new Pose with the position vector subtracted
     */
    public Pose sub(@NonNull Pose pose){
        return new Pose(this.x - pose.x, this.y - pose.y, this.heading - pose.heading);
    }

    /**
     * @param radians angle to rotate the positional vector with
     * @return a new Pose with the position vector rotated by the parameter
     */
    public Pose rotate(double radians){
        return new Pose(this.x * Math.cos(radians) - this.y * Math.sin(radians),
                        this.x * Math.sin(radians) + this.y * Math.cos(radians),
                        this.heading);
    }

    /**
     * @return pose heading in radians
     */
    public double getHeading() {
        return heading;
    }

    /**
     * @param heading rotation in radians
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * @return pose y coordinate in cm
     */
    public double getY() {
        return y;
    }

    /**
     * @param y coordinate in cm
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return pose x coordinate in cm
     */
    public double getX() {
        return x;
    }

    /**
     * @param x coordinate in cm
     */
    public void setX(double x) {
        this.x = x;
    }
}
