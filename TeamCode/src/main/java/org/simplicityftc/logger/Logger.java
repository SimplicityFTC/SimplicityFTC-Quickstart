package org.simplicityftc.logger;

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

    private Logger() {
    }

    /**
     * This method returns the singleton instance of the Logger.
     * @return The singleton instance of the Logger.
     */
    public static Logger getInstance() {
        return instance;
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
     * @param message The message to add to the log file.
     */
    public void add(LogMessage message) {
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
            //TODO ACTUALLY CHECK FORMAT I JUST THREW THIS TOGETHER WITH 0 BRAIN
            fileWriter.write(
                    SDF.format(new Date(System.currentTimeMillis()) + " ")
                        + message.getType() + " "
                        + message.getContent() + "\n");
            fileWriter.close();
        } catch (IOException ignored) { }
    }
}
