package org.simplicityftc.commandbase;

/**
 * Usage:
 * If using a boolean returning function:
 * new ConditionalCommand(() -> {return class.method();});
 */
public class ConditionalCommand extends Command {
    private final LambdaFunction<Boolean> function;

    public ConditionalCommand (LambdaFunction<Boolean> function) {
        this.function = function;
    }

    @Override
    public boolean run() {
        return function.run();
    }

    @Override
    public void log() {

    }

}