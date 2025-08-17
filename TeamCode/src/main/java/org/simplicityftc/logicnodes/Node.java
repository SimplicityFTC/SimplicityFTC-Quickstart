package org.simplicityftc.logicnodes;

import org.simplicityftc.commandbase.Command;
import org.simplicityftc.commandbase.LambdaFunction;

public class Node {
    private final String name;
    private LambdaFunction<Boolean> failDetection;
    private final Command command;
    private String failsafeName;
    private String nextName;

    public Node(String name, Command command) {
        this.name = name;
        this.command = command;
        this.failDetection = null;
        this.failsafeName = null;
        this.nextName = null;
    }

    /**
     * This method returns the name of the node.
     * @return The name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * This method stores the function that is used to check for failures.
     * @param failDetection This is the method that detects failures.
     * @return The current node instance.
     */
    public Node failIf(LambdaFunction<Boolean> failDetection) {
        this.failDetection = failDetection;
        return this;
    }

    /**
     * This method stores the name of the node to go to if fail is detected.
     * You can pass the name of a node that is created and added to AutonomousNodes
     * after this method call.
     * @param nodeName The name of the desired node.
     * @return The current node instance.
     */
    public Node onFail(String nodeName) {
        failsafeName = nodeName;
        return this;
    }

    /**
     * This method stores the name of the node to go to next.
     * You can pass the name of a node that is created and added to AutonomousNodes
     * after this method call.
     * @param nodeName The name of the desired node.
     * @return The current node instance.
     */
    public Node onSuccess(String nodeName) {
        nextName = nodeName;
        return this;
    }

    public LambdaFunction<Boolean> getFailDetection() {
        return failDetection;
    }

    public Command getCommand() {
        return command;
    }

    public String getFailsafeName() {
        return failsafeName;
    }

    public String getNextName() {
        return nextName;
    }
}
