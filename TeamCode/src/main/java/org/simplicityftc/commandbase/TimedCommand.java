package org.simplicityftc.commandbase;


import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Usage:
 * Allows any function and will return true if the timeout is reached
 * or the function returns true(unless it is not a boolean lambda)
 * new TimedCommand(() -> class.method(), timeoutSeconds);
 */
public class TimedCommand extends Command {
    private ElapsedTime timer;
    private final double timeoutSeconds;
    private final LambdaFunction<?> function;

    /**
     * Allows any function and will return true if the timeout is reached
     * @param function The function to run
     * @param timeoutSeconds The amount of time to wait
     */
    public TimedCommand(LambdaFunction<?> function, double timeoutSeconds) {
        this.function = function;
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Allows any function and will return true if the timeout is reached
     * @param commandName The name of the command
     * @param function The function to run
     * @param timeoutSeconds The amount of time to wait
     */
    public TimedCommand(String commandName, LambdaFunction<?> function, double timeoutSeconds) {
        super(commandName);
        this.function = function;
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Allows any function and will return true if the timeout is reached
     * @param function The function to run
     * @param timeoutSeconds The amount of time to wait
     */
    public TimedCommand(Runnable function, double timeoutSeconds) {
        this.function = () -> {
            function.run();
            return null;
        };
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Allows any function and will return true if the timeout is reached
     * @param commandName The name of the command
     * @param function The function to run
     * @param timeoutSeconds The amount of time to wait
     */
    public TimedCommand(String commandName, Runnable function, double timeoutSeconds) {
        super(commandName);
        this.function = () -> {
            function.run();
            return null;
        };
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public boolean run() {
        if(timer == null)
            timer = new ElapsedTime();

        //TODO: test if this works
        return timer.seconds() >= timeoutSeconds || function.run().equals(true);
    }
}