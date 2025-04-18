package org.simplicityftc.commandbase;

import org.simplicityftc.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class schedules and runs multiple types of commands
 */
public class CommandScheduler {
    private static CommandScheduler scheduler;
    private List<Command> commands;

    private CommandScheduler() {
        this.commands = new ArrayList<>();
    }

    /**
     * This function ensures there is only one active instance of the CommandScheduler and is used
     * to access said instance.
     * @return The unique instance of CommandScheduler.
     */
    public static CommandScheduler getInstance() {
        if (scheduler == null)
            scheduler = new CommandScheduler();
        return scheduler;
    }

    /**
     * This function clears the list of active commands, should usually be called in init.
     */
    public void reset() {
        commands.clear();
    }

    /**
     * This function schedules a command.
     * @param command The desired command to be scheduled.
     */
    public void schedule(Command command) {
        commands.add(command.clone());
        scheduleLog(command);
    }

    /**
     * This function removes a command from the list of scheduled commands.
     * @param commandName The name of the command to be removed.
     */
    public void remove(String commandName) {
        commands = commands.stream().filter(c -> !c.getCommandName().equals(commandName)).collect(Collectors.toList());
    }

    /**
     * This function writes a schedule log.
     * @param command The scheduled command.
     */
    private void scheduleLog(Command command) {
        Logger.getInstance().add(
                Logger.LogType.COMMAND,
                command.getCommandName().isEmpty() ?
                        command.getClass().getSimpleName() :
                        command.getCommandName() + " scheduled"
        );
    }

    /**
     * This function writes a removal log.
     * @param command The removed command.
     */
    private void removeLog(Command command) {
        Logger.getInstance().add(
                Logger.LogType.COMMAND,
                command.getCommandName().isEmpty() ?
                        command.getClass().getSimpleName() :
                        command.getCommandName() + " removed"
        );
    }

    /**
     * This function runs the list of scheduled commands.
     */
    public void run() {
        if (commands.isEmpty())
            return;

        commands = commands.stream()
                .filter(c -> {
                    if (c.run()) {
                        removeLog(c); // Run method for removed commands
                        return false; // Remove command
                    }
                    return true; // Keep command
                })
                .collect(Collectors.toList());
    }
}