package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

/**
 * Usage:
 * If using a boolean returning function:
 * new ConditionalCommand(() -> {return class.method();});
 */
public class ConditionalCommand extends Command {
    private final LambdaFunction<Boolean> function;

    /**
     * If using a boolean returning function:
     * @param function The function to run
     */
    public ConditionalCommand (LambdaFunction<Boolean> function) {
        this.function = function;
    }

    /**
     * If using a boolean returning function:
     * @param commandName The name of the command
     * @param function The function to run
     */
    public ConditionalCommand(String commandName, LambdaFunction<Boolean> function) {
        super(commandName);
        this.function = function;
    }

    @Override
    public boolean run() {
        return function.run();
    }

    @NonNull
    @Override
    public Command clone() {
        return new ConditionalCommand(getCommandName(), function);
    }
}