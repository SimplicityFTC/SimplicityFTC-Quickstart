package org.simplicityftc.logger;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * This class is used to write logs to a file.
 */
public class Logger {
    private static final Logger instance = new Logger();
    private File logFile = null;
    private FileWriter fileWriter = null;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss", Locale.US);
    private boolean isEnabled = false;

    /**
     * This enum is used to specify the type of log message.
     */
    public enum LogType {
        INFO,
        DEBUG,
        WARN,
        ERROR,
        COMMAND,
        NODE,
        VOLTAGE,
        OVER_CURRENT,
        REFRESH_RATE;

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
                case NODE:
                    return "[NODE]";
                case VOLTAGE:
                    return "[VOLTAGE]";
                case OVER_CURRENT:
                    return "[OVER_CURRENT]";
                case REFRESH_RATE:
                    return "[REFRESH_RATE]";
                default:
                    throw new IllegalArgumentException("How did you manage this? 💀");
            }
        }
    }

    private Logger() { }

    /**
     * This method returns the singleton instance of the Logger.
     * @return The singleton instance of the Logger.
     */
    public static Logger getInstance() {
        return instance;
    }

    /**
     * This method sets the isEnabled state of the logger for writing in a separate file.
     * The default state is false. This state does not affect the System.out logs, they
     * will always be enabled.
     * @param enable The desired state of the logger.
     */
    public void enableFileLogging(boolean enable) {
        isEnabled = enable;
    }

    /**
     * This method sets the name of the log file.
     * You do not need to add an extension.
     * In case of any extension, it will be removed and replaced with .txt.
     * @param fileName The name of the log file.
     */
    public void setLogFileName(String fileName) {
        // Cleans up any extra extensions
        List<String> fileNameParts = Arrays.asList(fileName.split("\\."));
        logFile = new File(fileNameParts.get(0) + ".txt");
    }

    /**
     * This method adds a message to the log file.
     * The log file name defaults as log.txt.
     * @param message The message to add to the log file.
     */
    public void add(LogType logType, String message) {
        System.out.println(message);
        if (!isEnabled) {
            return;
        }

        if (logFile == null) {
            logFile = new File("log.txt");
        }

        try {
            logFile.createNewFile();
        } catch (IOException ignored) { }



        try {
            if (fileWriter == null) {
                fileWriter = new FileWriter(logFile, true);
            }
            fileWriter.write(
                    logType.toString() + " "
                        + SDF.format(new Date(System.currentTimeMillis())) + " "
                        + message + "\n"
            );
            fileWriter.close();
        } catch (IOException ignored) { }
    }
}
