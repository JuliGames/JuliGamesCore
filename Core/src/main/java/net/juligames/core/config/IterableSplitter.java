package net.juligames.core.config;

import net.juligames.core.api.config.ConfigWriter;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The IterableSplitter class provides utility methods for splitting and processing iterables.
 * It allows splitting an iterable into strings, writing the split strings to a configuration,
 * and reading split strings from a configuration and interpreting them as objects of a given type.
 * This class is final and cannot be subclassed.
 *
 * @author Ture Bentzin
 * 10.01.2023
 */
public final class IterableSplitter {

    /**
     * Private constructor to prevent instantiation of the IterableSplitter class.
     */
    private IterableSplitter() {
    }

    /**
     * Splits the elements of the given iterable into strings using the provided interpreter and returns a list of the split strings.
     *
     * @param <T>         the type of the elements in the iterable
     * @param iterable    the iterable to be split
     * @param interpreter the interpreter to convert elements to strings
     * @return a list of strings representing the split elements
     * @throws NullPointerException if the iterable or interpreter parameter is null
     */
    @Contract(pure = true)
    public static <T> @NotNull List<String> simpleSplit(@NotNull Iterable<T> iterable, Interpreter<T> interpreter) {
        final ArrayList<String> data = new ArrayList<>();
        for (T t : iterable) {
            String reverse = interpreter.reverse(t);
            data.add(reverse);
        }
        return data;
    }

    /**
     * Splits the elements of the given iterable into strings using the provided interpreter
     * and returns a SplitCollectionConfigWriter that can be used to write the split strings to a configuration.
     *
     * @param <T>         the type of the elements in the iterable
     * @param iterable    the iterable to be split
     * @param interpreter the interpreter to convert elements to strings
     * @return a SplitCollectionConfigWriter for writing the split strings to a configuration
     * @throws NullPointerException if the iterable or interpreter parameter is null
     */
    @Contract(pure = true)
    public static <T> @NotNull SplitCollectionConfigWriter splitToWriter(Iterable<T> iterable, Interpreter<T> interpreter) {
        return new SplitCollectionConfigWriter(simpleSplit(iterable, interpreter));
    }

    /**
     * Splits the elements of the given iterable into strings using the provided interpreter
     * and writes the split strings to the given configuration under the specified key space.
     *
     * @param <T>           the type of the elements in the iterable
     * @param iterable      the iterable to be split
     * @param interpreter   the interpreter to convert elements to strings
     * @param configuration the configuration to write the split strings to
     * @param keySpace      the key space under which the split strings will be written
     * @throws NullPointerException if any of the iterable, interpreter, configuration, or keySpace parameters is null
     */
    public static <T> void splitAndWrite(Iterable<T> iterable, Interpreter<T> interpreter, Configuration configuration, String keySpace) {
        splitToWriter(iterable, interpreter).write(configuration, keySpace);
    }

    /**
     * Reads a collection of objects of the specified type from the given strings using the provided interpreter.
     * Any strings that cannot be interpreted as objects will be ignored.
     *
     * @param <T>         the type of the objects to read
     * @param strings     the strings to read the objects from
     * @param interpreter the interpreter to convert strings to objects
     * @return a collection of objects interpreted from the strings
     * @throws NullPointerException if the strings or interpreter parameter is null
     */
    public static <T> @NotNull Collection<T> tryReadStrings(@NotNull Iterable<String> strings, Interpreter<T> interpreter) {
        final Collection<T> ts = new ArrayList<>();
        for (String value : strings) {
            try {
                ts.add(interpreter.interpret(value));
            } catch (Exception ignored) {
                // Ignore strings that cannot be interpreted as objects
            }
        }
        return ts;
    }

    /**
     * Reads a collection of objects of the specified type from the values in the given configuration
     * that are associated with keys starting with the specified key space.
     *
     * @param <T>           the type of the objects to read
     * @param configuration the configuration to read the values from
     * @param keySpace      the key space indicating the keys to read the values from
     * @param interpreter   the interpreter to convert strings to objects
     * @return a collection of objects interpreted from the configuration values
     * @throws NullPointerException if any of the configuration, keySpace, or interpreter parameters is null
     */
    public static <T> @NotNull Collection<T> tryReadSplitCollection(@NotNull Configuration configuration, String keySpace, Interpreter<T> interpreter) {
        return tryReadStrings(
                configuration.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().startsWith(keySpace))
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toUnmodifiableSet()),
                interpreter);
    }

    /**
     * The SplitCollectionConfigWriter class provides a way to write split strings to a configuration.
     * It implements the ConfigWriter interface and is used by the splitToWriter method in the IterableSplitter class.
     */
    public static final class SplitCollectionConfigWriter implements ConfigWriter {

        private final List<String> data;

        /**
         * Constructs a SplitCollectionConfigWriter with the given list of split strings.
         *
         * @param simpleSplit the list of split strings to be written to a configuration
         * @throws NullPointerException if the simpleSplit parameter is null
         */
        public SplitCollectionConfigWriter(List<String> simpleSplit) {
            data = Collections.unmodifiableList(simpleSplit);
        }

        /**
         * Writes the split strings to the specified configuration under the given key space.
         *
         * @param configuration the configuration to write the split strings to
         * @param keySpace      the key space under which the split strings will be written
         * @throws NullPointerException if the configuration or keySpace parameter is null
         */
        @Override
        public void write(@NotNull Configuration configuration, @NotNull String keySpace) {
            for (int i = 0; i < data.size(); i++) {
                final String simple = data.get(i);
                final String key = keySpace + "_" + i;
                configuration.setString(key, simple);
            }
        }
    }
}