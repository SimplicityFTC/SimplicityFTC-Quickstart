package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

/**
 * Usage:
 * Waits until the passed boolean lambda function returns true
 */

public class WaitUntilCommand extends Command {
    private final LambdaFunction<Boolean> booleanFunction;

    /**
     * Waits until the passed boolean lambda function returns true
     * @param booleanFunction The function to run
     */
    public WaitUntilCommand (LambdaFunction<Boolean> booleanFunction) {
        this.booleanFunction = booleanFunction;
    }

    /**
     * Waits until the passed boolean lambda function returns true
     * @param commandName The name of the command
     * @param booleanFunction The function to run
     */
    public WaitUntilCommand (String commandName, LambdaFunction<Boolean> booleanFunction) {
        super(commandName);
        this.booleanFunction = booleanFunction;
    }

    @Override
    public boolean run() {
        return booleanFunction.run();
    }

    @NonNull
    @Override
    public Command clone() {
        return new WaitUntilCommand(getCommandName(), booleanFunction);
    }
}

