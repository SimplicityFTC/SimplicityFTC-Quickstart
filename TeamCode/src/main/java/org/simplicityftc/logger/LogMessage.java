package org.simplicityftc.logger;

import androidx.annotation.NonNull;

public class LogMessage {
    public enum LogType {
        INFO,
        DEBUG,
        WARN,
        ERROR,
        FATAL;

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
                case FATAL:
                    return "[FATAL]";
                default:
                    throw new IllegalArgumentException("How did you manage this? 🤣");
            }
        }
    }

    private LogType type;
    private String content;

    public LogMessage() {
        type = LogType.INFO;
        content = null;
    }

    public LogMessage setLogType(LogType type) {
        this.type = type;
        return this;
    }

    public LogMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return content;
    }

    public LogType getType() {
        return type;
    }
}
