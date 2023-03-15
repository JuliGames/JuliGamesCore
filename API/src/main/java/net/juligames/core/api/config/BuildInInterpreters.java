package net.juligames.core.api.config;

import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.DBData;
import net.juligames.core.api.jdbi.DataDAO;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public interface BuildInInterpreters {
    @Contract(pure = true)
    static @NotNull SimpleInterpreter<UUID> uuidInterpreter() {
        return UUID::fromString;
    }

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<URI> uriInterpreter() {
        return URI::create;
    }

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<URL> urlInterpreter() {
        return URL::new;
    }

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<File> fileInterpreter() {
        return File::new;
    }

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<Instant> instantInterpreter() {
        return Instant::parse;
    }

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<Date> dateInterpreter() {
        return s -> DateFormat.getDateInstance().parse(s);
    }

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<String> interpreter() {
        return s -> s;
    }

    @Contract(pure = true)
    static @NotNull Interpreter<Class<?>> clazzInterpreter() {
        return new Interpreter<>() {
            @Override
            public @NotNull Class<?> interpret(@NotNull String input) throws Exception {
                return Class.forName(input);
            }

            @Override
            public @NotNull String reverse(@NotNull Class<?> tClass) {
                return tClass.getName();
            }
        };
    }




    @ApiStatus.AvailableSince("1.5")
    @Contract(value = "_ -> new", pure = true)
    static @NotNull Interpreter<Configuration> onlineConfigurationInterpreter(boolean createIfNotExists) {
        return new Interpreter<>() {
            @Override
            public @NotNull Configuration interpret(@NotNull String input) throws Exception {
                if (!createIfNotExists && !API.get().getConfigurationApi().exists(input)) {
                    throw new NoSuchElementException(input + " has no associated configuration!");
                }
                return API.get().getConfigurationApi().getOrCreate(input);
            }

            @Override
            public @NotNull String reverse(@NotNull Configuration configuration) {
                return configuration.getName();
            }
        };
    }

    @Contract(value = "_ -> new", pure = true)
    @ApiStatus.AvailableSince("1.5")
    static @NotNull Interpreter<Duration> durationInterpreter(TemporalUnit unit) {
        return new Interpreter<>() {
            @Override
            public @NotNull Duration interpret(String input) throws DateTimeException, ArithmeticException, NumberFormatException {
                return Duration.of(Long.parseLong(input), unit);
            }

            @Override
            public @NotNull String reverse(Duration duration) {
                return String.valueOf(duration.get(unit));
            }
        };
    }

    @Contract(value = " -> new", pure = true)
    @ApiStatus.Experimental
    @ApiStatus.AvailableSince("1.5")
    static @NotNull Interpreter<DBData> dbDataInterpreter() {
        return new Interpreter<>() {
            @Override
            public @NotNull DBData interpret(String input) throws Exception {
                return Objects.requireNonNull(API.get().getSQLManager().withExtension(DataDAO.class, extension ->
                        extension.select(input)));
            }

            @Override
            public @NotNull String reverse(DBData dbData) {
                return dbData.getKey();
            }
        };
    }

}
