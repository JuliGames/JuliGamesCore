package net.juligames.core.api.config.mapbacked;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.NotNull;

import java.util.Dictionary;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @apiNote this can be used for {@link java.util.Hashtable}s or {@link java.util.Properties}s
 * @author Ture Bentzin
 * 11.03.2023
 */
public class DictionaryFeedInterpreter<E> implements Interpreter<MapPart<E>> {

    private final @NotNull Supplier<Dictionary<String, E>> dictionarySupplier;
    private final @NotNull BiFunction<String, Supplier<Dictionary<String, E>>, ? extends MapPart<E>> dictionaryPartFactory;


    public DictionaryFeedInterpreter(@NotNull Supplier<Dictionary<String, E>> dictionarySupplier) {
        this.dictionarySupplier = dictionarySupplier;
        this.dictionaryPartFactory = DictionaryPart::new;
    }

    public DictionaryFeedInterpreter(@NotNull Supplier<Dictionary<String, E>> dictionarySupplier,
                                     @NotNull BiFunction<String, Supplier<Dictionary<String, E>>, MapPart<E>> dictionaryPartFactory) {
        this.dictionarySupplier = dictionarySupplier;
        this.dictionaryPartFactory = dictionaryPartFactory;
    }


    @Override
    public @NotNull MapPart<E> interpret(@NotNull String input) {
        return new DictionaryPart<>(input, dictionarySupplier);
    }

    @Override
    public @NotNull String reverse(@NotNull MapPart<E> eMapPart) {
        return eMapPart.key();
    }


    private record DictionaryPart<E>(String key,
                                     Supplier<Dictionary<String, E>> dictionarySupplier) implements MapPart<E> {

        @Override
        public @NotNull String key() {
            return key;
        }

        @Override
        public @NotNull E get() {
            return dictionarySupplier.get().get(key());
        }
    }
}
