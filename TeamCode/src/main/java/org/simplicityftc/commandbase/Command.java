package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

import org.simplicityftc.logger.Loggable;

public abstract class Command implements Cloneable, Loggable {
    /**
     * Runs the command
     * @return true if the command finished, false if it needs to be run again
     */
    public abstract boolean run();
    /**
     * Clones the command
     * @return the cloned command
     */
    @NonNull
    @Override
    public Command clone() {
        try {
            return (Command) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public abstract void log();
}