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

    public TimedCommand(LambdaFunction<?> function, double timeoutSeconds)
    {
        this.function = function;
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