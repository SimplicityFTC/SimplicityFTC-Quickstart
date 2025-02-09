package org.simplicityftc.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class Logger {
    private static final Logger instance = new Logger();
    private File logFile = null;
    private FileWriter fileWriter = null;

    public static Logger getInstance() {
        return instance;
    }

    private Logger() {
    }

    public void setLogFileName(String fileName) {
        if (fileName.endsWith(".txt")) {
            logFile = new File(fileName);
            return;
        }
        logFile = new File(fileName + ".txt");
    }

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
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Date(System.currentTimeMillis()))
                        + message.getType()
                        + message.getContent());
            fileWriter.close();
        } catch (IOException ignored) { }
    }
}
