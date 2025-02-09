package org.simplicityftc.commandbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Usage:
 * new SequentialCommand(new CommandType(() -> {insert usual command contents},
 * 						new CommandType(() -> {insert usual command contents},
 * 						...);
 * CommandType should be replaced by whatever type of command you want (ConditionalCommand, InstantCommand, ParallelCommand or SequentialCommand);
 */
public class SequentialCommand extends Command {
    private final ArrayList<Command> commands;

    public SequentialCommand(List<Command> commands)
    {
        this.commands = new ArrayList<>(commands);
    }

    public SequentialCommand(Command... commands)
    {
        this(Arrays.asList(commands));
    }

    @Override
    public boolean run() {
        if (commands.isEmpty())
            return true;

        if (commands.get(0).run())
            commands.remove(0);

        return false;
    }

    @Override
    public void log() {

    }
}