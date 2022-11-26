package net.juligames.core.api.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public interface BuildInInterpreters {
    @Contract(pure = true)
    static @NotNull SimpleInterpreter<UUID> uuidInterpreter() {return UUID::fromString;}

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<URI> uriInterpreter() {return URI::create;}

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<URL> urlInterpreter() {return URL::new;}

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<File> fileInterpreter() {return File::new;}

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<Instant> instantInterpreter() {return Instant::parse;}

    @Contract(pure = true)
    static @NotNull SimpleInterpreter<Date> dateInterpreter() {return s -> DateFormat.getDateInstance().parse(s);}



}
