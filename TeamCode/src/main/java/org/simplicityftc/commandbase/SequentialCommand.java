package org.simplicityftc.commandbase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Usage:
 * new SequentialCommand(new CommandType(() -> {insert usual command contents},
 * 						new CommandType(() -> {insert usual command contents},
 * 						...);
 * CommandType should be replaced by whatever type of command you want (ConditionalCommand, InstantCommand, ParallelCommand or SequentialCommand);
 */
public class SequentialCommand extends Command {
    private final List<Command> commands;

    /**
     * If using a list of commands:
     * @param commands The list of commands to run
     */
    public SequentialCommand(List<Command> commands)
    {
        this.commands = new ArrayList<>(commands);
    }

    /**
     * If using an array of commands:
     * @param commandName The name of the command
     * @param commands The array of commands to run
     */
    public SequentialCommand(String commandName, List<Command> commands)
    {
        super(commandName);
        this.commands = new ArrayList<>(commands);
    }

    /**
     * If using an array of commands:
     * @param commands The array of commands to run
     */
    public SequentialCommand(Command... commands)
    {
        this(Arrays.asList(commands));
    }

    /**
     * If using an array of commands:
     * @param commandName The name of the command
     * @param commands The array of commands to run
     */
    public SequentialCommand(String commandName, Command... commands)
    {
        this(commandName, Arrays.asList(commands));
    }

    @Override
    public boolean run() {
        if (commands.isEmpty())
            return true;

        if (commands.get(0).run())
            commands.remove(0);

        return false;
    }

    @NonNull
    @Override
    public Command clone() {
        return new SequentialCommand(getCommandName(), commands.stream().map(Command::clone).collect(Collectors.toList()));
    }
}