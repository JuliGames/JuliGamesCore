package net.juligames.core.config;

import net.juligames.core.api.config.ConfigWriter;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 10.01.2023
 */
public final class IterableSplitter {

    private IterableSplitter() {
    }


    @Contract(pure = true)
    public static <T> @NotNull List<String> simpleSplit(@NotNull Iterable<T> iterable, Interpreter<T> interpreter) {
        final ArrayList<String> data = new ArrayList<>();
        for (T t : iterable) {
            String reverse = interpreter.reverse(t);
            data.add(reverse);
        }
        return data;
    }

    @Contract(pure = true)
    public static <T> @NotNull SplitCollectionConfigWriter splitToWriter(Iterable<T> iterable, Interpreter<T> interpreter) {
        return new SplitCollectionConfigWriter(simpleSplit(iterable, interpreter));
    }

    public static <T> void splitAndWrite(Iterable<T> iterable, Interpreter<T> interpreter, Configuration configuration, String keySpace) {
        splitToWriter(iterable, interpreter).write(configuration, keySpace);
    }

    public static <T> @NotNull Collection<T> tryReadStrings(@NotNull Iterable<String> strings, Interpreter<T> interpreter) {
        final Collection<T> ts = new ArrayList<>();
        for (String value : strings)
            try {
                ts.add(interpreter.interpret(value));
            } catch (Exception ignored) {
            }
        return ts;
    }

    public static <T> @NotNull Collection<T> tryReadSplitCollection(@NotNull Configuration configuration, String keySpace, Interpreter<T> interpreter) {
        return tryReadStrings(configuration.entrySet().stream().filter(entry ->
                        entry.getKey().startsWith(keySpace)).map(Map.Entry::getValue)
                .collect(Collectors.toUnmodifiableSet()), interpreter);
    }

    public static final class SplitCollectionConfigWriter implements ConfigWriter {

        final List<String> data;

        public SplitCollectionConfigWriter(List<String> simpleSplit) {
            data = Collections.unmodifiableList(simpleSplit);
        }

        @Override
        public void write(Configuration configuration, String keyspace) {
            for (int i = 0; i < data.size(); i++) {
                final String simple = data.get(i);
                final String key = keyspace + "_" + i;
                configuration.setString(key, simple);
            }
        }
    }
}
