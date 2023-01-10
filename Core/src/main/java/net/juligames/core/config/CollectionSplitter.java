package net.juligames.core.config;

import net.juligames.core.api.config.ConfigWriter;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ture Bentzin
 * 10.01.2023
 */
public final class CollectionSplitter {

    private CollectionSplitter() {
    }


    @Contract(pure = true)
    public static <T> @NotNull List<String> simpleSplit(@NotNull Collection<T> collection, Interpreter<T> interpreter) {
        final ArrayList<String> data = new ArrayList<>();
        for (T t : collection) {
            String reverse = interpreter.reverse(t);
            data.add(reverse);
        }
        return data;
    }

    @Contract(pure = true)
    public static <T> @NotNull SplitCollectionConfigWriter splitToWriter(Collection<T> collection, Interpreter<T> interpreter) {
        return new SplitCollectionConfigWriter(simpleSplit(collection, interpreter));
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
                final String key = keyspace + "." + i;
                configuration.setString(key, simple);
            }
        }
    }
}
