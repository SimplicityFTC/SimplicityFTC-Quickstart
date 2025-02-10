package org.simplicityftc.logger;

import androidx.annotation.NonNull;

/**
 * This class is used to represent a log message.
 */
public class LogMessage {
    /**
     * This enum is used to represent the type of log message.
     */
    public enum LogType {
        INFO,
        DEBUG,
        WARN,
        ERROR,
        COMMAND,
        VOLTAGE,
        OVER_CURRENT;

        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case INFO:
                    return "[INFO]";
                case DEBUG:
                    return "[DEBUG]";
                case WARN:
                    return "[WARN]";
                case ERROR:
                    return "[ERROR]";
                case COMMAND:
                    return "[COMMAND]";
                case VOLTAGE:
                    return "[VOLTAGE]";
                case OVER_CURRENT:
                    return "[OVER_CURRENT]";
                default:
                    throw new IllegalArgumentException("How did you manage this? 🤣");
            }
        }
    }

    private LogType type;
    private String content;

    /**
     * This constructor is used to create a new log message.
     */
    public LogMessage() {
        type = LogType.INFO;
        content = null;
    }

    /**
     * This constructor is used to create a new log message.
     * @param type The type of log message.
     * @return The new log message.
     */
    public LogMessage setLogType(LogType type) {
        this.type = type;
        return this;
    }

    /**
     * This method is used to set the content of the log message.
     * @param content The content of the log message.
     * @return The new log message.
     */
    public LogMessage setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * This method is used to get the content of the log message.
     * @return The content of the log message.
     */
    public String getContent() {
        return content;
    }

    /**
     * This method is used to get the type of the log message.
     * @return The type of the log message.
     */
    public LogType getType() {
        return type;
    }
}
