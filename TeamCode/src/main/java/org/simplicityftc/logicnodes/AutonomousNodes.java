package org.simplicityftc.logicnodes;

import org.simplicityftc.commandbase.CommandScheduler;
import org.simplicityftc.commandbase.InstantCommand;
import org.simplicityftc.commandbase.SequentialCommand;
import org.simplicityftc.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class AutonomousNodes {
    private Map<String, Node> nodeMap;
    private Node currentNode;
    private static AutonomousNodes INSTANCE;
    private boolean scheduledNode;
    private boolean reachedEnd;

    private AutonomousNodes() {
        nodeMap = new HashMap<>();
        scheduledNode = false;
        reachedEnd = false;
        currentNode = null;
    }

    /**
     * This method returns the singleton instance of the Autonomous nodes class.
     * @return The unique instance of the AutoNodes class.
     */
    public static AutonomousNodes getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutonomousNodes();
            return INSTANCE;
        }
        return INSTANCE;
    }

    /**
     * This method adds a node to the Autonomous graph. Every node must have a unique name.
     * @param node The desired node to be added.
     */
    public void add(Node node) {
        if (nodeMap.containsKey(node.getName())) {
            Logger.getInstance().add(Logger.LogType.ERROR, "You have added multiple nodes with the name " + node.getName());
            throw new IllegalArgumentException("You have added multiple nodes with the name " + node.getName());
        }
        nodeMap.put(node.getName(), node);
    }

    /**
     * This method clears all of the nodes.
     */
    public void clear() {
        nodeMap = new HashMap<>();
    }

    /**
     * This method sets the starting node of the autonomous.
     * @param startNode The name of the starting node.
     */
    public void selectStartingNode(String startNode) {
        this.currentNode = nodeMap.getOrDefault(startNode, null);
        if (this.currentNode == null) {
            Logger.getInstance().add(Logger.LogType.ERROR, "You cannot set a non-existent node as start node");
            throw new IllegalArgumentException("You cannot set a non-existent node as start node");
        }
    }

    /**
     * This method adds a note to the map and sets it as the starting node of the autonomous.
     * @param startNode The Node object to be added to the map and set as starting node.
     */
    public void selectStartingNode(Node startNode) {
        add(startNode);
        this.currentNode = startNode;
    }

    /**
     * This method runs the autonomous logic nodes.
     */
    public void run() {
        if (reachedEnd) return;

        if (currentNode == null) {
            Logger.getInstance().add(Logger.LogType.INFO, "Reached end of logic node tree.");
            reachedEnd = true;
            return;
        }

        if (scheduledNode) {
            // If the node has no failsafe, all we can do is wait for the command to finish executing.
            if (currentNode.getFailDetection() == null) return;

            // If a fail is encountered this runs
            if (currentNode.getFailDetection().run() == true) {
                // Removes the current node's command
                CommandScheduler.getInstance().remove(currentNode.getName());
                Logger.getInstance().add(Logger.LogType.INFO, currentNode.getName() + " has failed.");

                // If the node either has an invalid failsafe or none at all it throws an exception
                if (currentNode.getFailsafeName() == null) {
                    Logger.getInstance().add(Logger.LogType.ERROR, "The " + currentNode.getName() + " node has a fail detection method but no failsafe node.");
                    throw new RuntimeException("The " + currentNode.getName() + " node has a fail detection method but no failsafe node.");
                }
                if (!nodeMap.containsKey(currentNode.getFailsafeName())) {
                    Logger.getInstance().add(Logger.LogType.ERROR, currentNode.getName() + "'s failsafe hasn't been added to the AutonomousNodes.");
                    throw new RuntimeException(currentNode.getName() + "'s failsafe hasn't been added to the AutonomousNodes.");
                }

                // Schedules the failsafe node
                currentNode = nodeMap.get(currentNode.getFailsafeName());
                scheduledNode = false;
            }
        } else {
            // This schedules a sequential command with the name of the current node (for the sake of easy removal)
            // that first executes the user's command and then changes the current node to the next.
            CommandScheduler.getInstance().schedule(
                    new SequentialCommand(currentNode.getName(),
                            currentNode.getCommand(),
                            new InstantCommand(() ->
                                {
                                    currentNode = nodeMap.getOrDefault(currentNode.getNextName(), null);
                                    scheduledNode = false;
                                }
                            )
                    )
            );
            scheduledNode = true;
        }
    }

}
