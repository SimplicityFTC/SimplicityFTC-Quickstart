package org.simplicityftc.commandbase;

import java.util.ArrayList;

/**
 * This class schedules and runs multiple types of commands
 */
public class CommandScheduler
{
    private static CommandScheduler scheduler;
    private final ArrayList<Command> commands;
    private final ArrayList<Command> removedCommands;

    private CommandScheduler()
    {
        this.commands = new ArrayList<>();
        this.removedCommands = new ArrayList<>();
    }

    /**
     * This function ensures there is only one active instance of the CommandScheduler and is used
     * to access said instance.
     * @return The unique instance of CommandScheduler.
     */
    public static CommandScheduler getInstance()
    {
        if (scheduler == null)
            scheduler = new CommandScheduler();
        return scheduler;
    }

    /**
     * This function clears the list of active commands, should usually be called in init.
     */
    public void reset()
    {
        commands.clear();
    }

    /**
     * This function schedules a command.
     * @param command The desired command to be scheduled.
     */
    public void schedule(Command command)
    {
        commands.add(command.clone());
    }

    /**
     * This function removes an already scheduled command from the scheduler.
     * @param command The desired command to be removed.
     */
    public void remove(Command command)
    {
        commands.remove(command);
    }

    /**
     * This function runs the list of scheduled commands.
     */
    public void run()
    {

        if (commands.isEmpty())
            return;

        for (Command command : commands) {
            if (command.run())
                removedCommands.add(command);
        }

        for (Command command : removedCommands)
            commands.remove(command);
        removedCommands.clear();
    }
}