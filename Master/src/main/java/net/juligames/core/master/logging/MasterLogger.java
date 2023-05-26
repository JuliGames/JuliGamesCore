package net.juligames.core.master.logging;

import de.bentzin.tools.logging.JavaLogger;
import de.bentzin.tools.logging.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ture Bentzin
 * 17.11.2022
 */
public final class MasterLogger extends JavaLogger {

    @ApiStatus.AvailableSince("1.6")
    public static void setupJavaLogging() {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("<yyyy/MM/dd> HH:mm:ss");

    public MasterLogger(String name, @NotNull Logger parent, java.util.logging.@NotNull Logger logger) {
        super(name, parent, logger);
        logger.addHandler(LogFileHandlerManager.generateFileHandler());
    }

    public MasterLogger(String name, java.util.logging.@NotNull Logger logger) {
        super(name, logger);
        logger.addHandler(LogFileHandlerManager.generateFileHandler());
    }

    @Override
    public void log(String message, @NotNull LogLevel logLevel) {
        super.log(timeAndDate(message), logLevel);
    }

    public @NotNull String timeAndDate(String message) {
        LocalDateTime now = LocalDateTime.now();
        String prefix = now.format(FORMATTER);
        return "[" + prefix + "]: " + message;
    }
}
