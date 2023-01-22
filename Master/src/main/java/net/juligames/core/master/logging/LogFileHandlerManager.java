package net.juligames.core.master.logging;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * @author Ture Bentzin
 * 22.01.2023
 */
public class LogFileHandlerManager {
    @Contract(" -> new")
    public static @NotNull FileHandler generateFileHandler() {
        try {
            FileHandler fileHandler = new FileHandler("latest.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            return fileHandler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
