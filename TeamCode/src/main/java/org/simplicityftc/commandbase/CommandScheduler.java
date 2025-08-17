package org.simplicityftc.commandbase;

import org.simplicityftc.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class schedules and runs multiple types of commands
 */
public class CommandScheduler {
    private static CommandScheduler INSTANCE;
    private List<Command> commands;
    private Map<String, List<String>> conflictMap;

    private CommandScheduler() {
        this.commands = new ArrayList<>();
        this.conflictMap = new HashMap<>();
    }

    /**
     * This method ensures there is only one active instance of the CommandScheduler and is used
     * to access said instance.
     * @return The unique instance of CommandScheduler.
     */
    public static CommandScheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandScheduler();
        }
        return INSTANCE;
    }

    /**
     * This method clears both the commands and the declared conflicts.
     */
    public void reset() {
        clearCommands();
        clearConflicts();
    }

    /**
     * This method clears the list of active commands.
     */
    public void clearCommands() {
        commands.clear();
    }

    /**
     * This function schedules a command and removes any declared conflicts from the scheduler.
     * @param command The desired command to be scheduled.
     */
    public void schedule(Command command) {
        List<String> conflicts = conflictMap.getOrDefault(command.getCommandName(), new ArrayList<>());

        if (conflicts != null && !conflicts.isEmpty()) {
            remove(conflicts);
        }

        commands.add(command.clone());
        scheduleLog(command);
    }

    /**
     * This function removes a command from the list of scheduled commands.
     * @param commandName The name of the command to be removed.
     */
    private void remove(String commandName) {
        commands = commands.stream().filter(c -> {
            if (c.getCommandName() != null)
                return !c.getCommandName().equals(commandName);
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * This function removes one or more commands from the list of scheduled commands.
     * @param commandNames The name of the commands to be removed.
     */
    public void remove(String... commandNames) {
        for (String commandName : commandNames) {
            remove(commandName);
        }
    }

    /**
     * This function removes one or more commands from the list of scheduled commands.
     * @param commandNames The name of the commands to be removed.
     */
    private void remove(List<String> commandNames) {
        for (String commandName : commandNames) {
            remove(commandName);
        }
    }

    /**
     * This function writes a schedule log.
     * @param command The scheduled command.
     */
    private void scheduleLog(Command command) {
        Logger.getInstance().add(
                Logger.LogType.COMMAND,
                command.getCommandName() == null ?
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
                command.getCommandName() == null ?
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
                        removeLog(c); // Write remove log
                        return false; // Remove command
                    }
                    return true; // Keep command
                })
                .collect(Collectors.toList());
    }

    /**
     * This function establishes a group of commands that cannot run simultaneously. When one
     * of these commands is added, the scheduler tries to remove all other ones from the list
     * of active commands.
     * @param commandNames Enumeration of command names that should not run concurrently.
     */
    public void addConflictGroup(String... commandNames) {
        addConflictGroup(Arrays.asList(commandNames));
    }

    /**
     * This function establishes a group of commands that cannot run simultaneously. When one
     * of these commands is added, the scheduler tries to remove all other ones from the list
     * of active commands.
     * @param commandNames Enumeration of command names that should not run concurrently.
     */
    private void addConflictGroup(List<String> commandNames) {
        for (String commandName : commandNames) {
            conflictMap.put(commandName, commandNames.stream().filter(c -> !c.equals(commandName)).collect(Collectors.toList()));
        }
    }

    public void clearConflicts() {
        conflictMap = new HashMap<>();
    }
}