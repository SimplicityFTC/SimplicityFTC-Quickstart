package org.simplicityftc.commandbase;

/**
 * Usage:
 * new InstantCommand(() -> class.method());
 *
 * new InstantCommand(() -> {class.method();
 *                           class.method2()});
 */


public class InstantCommand extends Command {

    private final LambdaFunction<Void> voidFunction;

    public InstantCommand (LambdaFunction<Void> voidFunction)
    {
        this.voidFunction = voidFunction;
    }

    @Override
    public boolean run()
    {
        voidFunction.run();
        return true;
    }

    @Override
    public void log() {

    }
}

