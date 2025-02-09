package org.simplicityftc.commandbase;

/**
 * Usage:
 * Waits until the passed boolean lambda function returns true
 */

public class WaitUntilCommand extends Command {
    private final LambdaFunction<Boolean> booleanFunction;

    public WaitUntilCommand (LambdaFunction<Boolean> booleanFunction) {
        this.booleanFunction = booleanFunction;
    }

    @Override
    public boolean run() {
        return booleanFunction.run();
    }

    @Override
    public void log() {

    }
}

