package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

/**
 * Usage:
 * new InstantCommand(() -> class.method());
 * new InstantCommand(() -> {class.method();
 *                           class.method2()});
 */
public class InstantCommand extends Command {

    private final LambdaFunction<Void> voidFunction;

    /**
     * InstantCommand
     * @param voidFunction The function to run
     */
    public InstantCommand (LambdaFunction<Void> voidFunction) {
        this.voidFunction = voidFunction;
    }

    /**
     * InstantCommand
     * @param commandName The name of the command
     * @param voidFunction The function to run
     */
    public InstantCommand (String commandName, LambdaFunction<Void> voidFunction) {
        super(commandName);
        this.voidFunction = voidFunction;
    }

    /**
     * InstantCommand
     * @param voidFunction The function to run
     */
    public InstantCommand(Runnable voidFunction) {
        this.voidFunction = () -> {
            voidFunction.run();
            return null;
        };
    }

    /**
     * InstantCommand
     * @param commandName The name of the command
     * @param voidFunction The function to run
     */
    public InstantCommand(String commandName, Runnable voidFunction) {
        super(commandName);
        this.voidFunction = () -> {
            voidFunction.run();
            return null;
        };
    }

    @Override
    public boolean run() {
        voidFunction.run();
        return true;
    }

    @NonNull
    @Override
    public Command clone() {
        return new InstantCommand(getCommandName(), voidFunction);
    }
}

