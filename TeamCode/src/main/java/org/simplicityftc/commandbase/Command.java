package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

public abstract class Command implements Cloneable {
    private String commandName = null;
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
    public abstract Command clone();

    /**
     * Gets the name of the command. If the command doesn't have a name, it returns null.
     * @return the name of the command or null
     */
    public String getCommandName() {
        return commandName;
    }
}