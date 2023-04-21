package net.juligames.core.api.config.mapbacked;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Dictionary;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 11.03.2023
 * @apiNote this can be used for {@link java.util.Hashtable}s or {@link java.util.Properties}s
 */
@ApiStatus.AvailableSince("1.5")
public class DictionaryFeedInterpreter<E> implements Interpreter<MapPart<E>> {

    private final @NotNull Supplier<Dictionary<String, E>> dictionarySupplier;
    private final @NotNull BiFunction<String, Supplier<Dictionary<String, E>>, ? extends MapPart<E>> dictionaryPartFactory;

    /**
     * Constructs a new {@link DictionaryFeedInterpreter} instance with the given dictionary supplier.
     *
     * @param dictionarySupplier the supplier to use to provide the backing dictionary
     */
    public DictionaryFeedInterpreter(@NotNull Supplier<Dictionary<String, E>> dictionarySupplier) {
        this.dictionarySupplier = dictionarySupplier;
        this.dictionaryPartFactory = DictionaryPart::new;
    }

    /**
     * Constructs a new {@link DictionaryFeedInterpreter} instance with the given dictionary supplier and map part factory.
     *
     * @param dictionarySupplier the supplier to use to provide the backing dictionary
     * @param dictionaryPartFactory the function to use to create map parts based on the dictionary and the key
     */
    public DictionaryFeedInterpreter(@NotNull Supplier<Dictionary<String, E>> dictionarySupplier,
                                     @NotNull BiFunction<String, Supplier<Dictionary<String, E>>, MapPart<E>> dictionaryPartFactory) {
        this.dictionarySupplier = dictionarySupplier;
        this.dictionaryPartFactory = dictionaryPartFactory;
    }

    /**
     * Interprets the given input string as a map part key and returns a corresponding map part.
     *
     * @param input the input string to interpret as a key
     * @return the corresponding map part
     */
    @Override
    public @NotNull MapPart<E> interpret(@NotNull String input) {
        return fabricate(input);
    }

    /**
     * Returns the factory function used to create map parts based on the dictionary and the key.
     *
     * @return the factory function used to create map parts
     */
    public BiFunction<String, Supplier<Dictionary<String, E>>, ? extends MapPart<E>> getDictionaryPartFactory() {
        return dictionaryPartFactory;
    }

    /**
     * Creates a new map part with the given key and dictionary supplier using the factory function.
     *
     * @param key the key of the new map part
     * @param dictionarySupplier the dictionary supplier to use for the new map part
     * @return the newly created map part
     */
    public @NotNull MapPart<E> fabricate(@NotNull String key, @NotNull Supplier<Dictionary<String, E>> dictionarySupplier) {
        return dictionaryPartFactory.apply(key, dictionarySupplier);
    }

    /**
     * Creates a new {@code MapPart} instance with the given key and using the current dictionary supplier and factory.
     *
     * @param key the key for the new {@code MapPart} instance
     * @return a new {@code MapPart} instance
     */
    protected @NotNull MapPart<E> fabricate(@NotNull String key) {
        return dictionaryPartFactory.apply(key, dictionarySupplier);
    }

    /**
     * Returns the key for the given {@code MapPart} instance.
     *
     * @param eMapPart the {@code MapPart} instance to get the key for
     * @return the key for the given {@code MapPart} instance
     */
    @Override
    public @NotNull String reverse(@NotNull MapPart<E> eMapPart) {
        return eMapPart.key();
    }

    /**
     * A private record that represents a map part backed by a dictionary.
     *
     * @param <E> the type of values stored in the dictionary
     */
    private record DictionaryPart<E>(
            String key,
            Supplier<Dictionary<String, E>> dictionarySupplier
    ) implements MapPart<E> {

        /**
         * Returns the key for this map part.
         *
         * @return the key for this map part
         */
        @Override
        public @NotNull String key() {
            return key;
        }

        /**
         * Returns the value for this map part by looking up the key in the backing dictionary.
         *
         * @return the value for this map part
         */
        @Override
        public @NotNull E get() {
            return dictionarySupplier.get().get(key());
        }
    }

}
