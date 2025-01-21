package org.simplicityftc.commandbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Usage:
 * new ParallelCommand(new CommandType(() -> {insert usual command contents},
 * 						new CommandType(() -> {insert usual command contents},
 * 						...);
 * CommandType should be replaced by whatever type of command you want (ConditionalCommand, InstantCommand, ParallelCommand or SequentialCommand);
 */
public class ParallelCommand extends Command {
    private final ArrayList<Command> commands;
    private ArrayList<Command> removedCommands;

    public ParallelCommand (List<Command> commands)
    {
        this.commands = new ArrayList<>(commands);
    }

    public ParallelCommand (Command... commands)
    {
        this(Arrays.asList(commands));
    }

    @Override
    public boolean run()
    {
        if (commands.isEmpty())
            return true;

        for (Command command : commands)
            if (command.run())
                removedCommands.add(command);

        for (Command command : removedCommands)
            commands.remove(command);
        removedCommands.clear();

        return false;
    }
}