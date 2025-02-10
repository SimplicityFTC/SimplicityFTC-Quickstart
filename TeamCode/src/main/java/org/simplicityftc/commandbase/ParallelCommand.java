package org.simplicityftc.commandbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Usage:
 * new ParallelCommand(new CommandType(() -> {insert usual command contents},
 * 						new CommandType(() -> {insert usual command contents},
 * 						...);
 * CommandType should be replaced by whatever type of command you want (ConditionalCommand, InstantCommand, ParallelCommand or SequentialCommand);
 */
public class ParallelCommand extends Command {
    private List<Command> commands;

    /**
     * If using a list of commands:
     * @param commandName The name of the command
     * @param commands The list of commands to run
     */
    public ParallelCommand(String commandName, List<Command> commands) {
        super(commandName);
        this.commands = new ArrayList<>(commands);
    }

    /**
     * If using a list of commands:
     * @param commands The list of commands to run
     */
    public ParallelCommand(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    /**
     * If using an array of commands:
     * @param commandName The name of the command
     * @param commands The array of commands to run
     */
    public ParallelCommand(String commandName, Command... commands) {
        this(commandName, Arrays.asList(commands));
    }

    /**
     * If using an array of commands:
     * @param commands The array of commands to run
     */
    public ParallelCommand(Command... commands) {
        this(Arrays.asList(commands));
    }

    @Override
    public boolean run() {
        if (commands.isEmpty())
            return true;

        commands = commands.stream()
                .filter(c -> !c.run())
                .collect(Collectors.toList());

        return false;
    }
}