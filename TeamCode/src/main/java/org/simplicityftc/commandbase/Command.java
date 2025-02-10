package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

public abstract class Command implements Cloneable {
    private String commandName = "";
    protected Command(String commandName) {
        this.commandName = commandName;
    }

    protected Command() { }
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

    public String getCommandName() {
        return commandName;
    }
}