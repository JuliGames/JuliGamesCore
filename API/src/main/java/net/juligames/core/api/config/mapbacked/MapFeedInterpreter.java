package net.juligames.core.api.config.mapbacked;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * This {@link Interpreter} allows to interpret keys from the config that get resolved using a given {@link Map}.
 * A known implementation is the {@link EnvironmentVariableInterpreter}
 *
 * @author Ture Bentzin
 * 07.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class MapFeedInterpreter<E> implements Interpreter<MapPart<E>> {

    private final @NotNull Supplier<Map<String, E>> mapSupplier;
    private final @NotNull BiFunction<String, Supplier<Map<String, E>>, ? extends MapPart<E>> mapPartFactory;


    public MapFeedInterpreter(@NotNull Supplier<Map<String, E>> mapSupplier) {
        this.mapSupplier = mapSupplier;
        this.mapPartFactory = MapPartImpl::new;
    }

    public MapFeedInterpreter(@NotNull Supplier<Map<String, E>> mapSupplier,
                              @NotNull BiFunction<String, Supplier<Map<String, E>>, ? extends MapPart<E>> mapPartFactory) {
        this.mapSupplier = mapSupplier;
        this.mapPartFactory = mapPartFactory;
    }


    @Override
    public @NotNull MapPart<E> interpret(@NotNull String input) {
        return fabricate(input);
    }

    @Override
    public @NotNull String reverse(@NotNull MapPart<E> eMapPart) {
        return eMapPart.key();
    }

    public @NotNull BiFunction<String, Supplier<Map<String, E>>, ? extends MapPart<E>> getMapPartFactory() {
        return mapPartFactory;
    }

    public @NotNull MapPart<E> fabricate(@NotNull String key, @NotNull Supplier<Map<String, E>> mapSupplier) {
        return mapPartFactory.apply(key,mapSupplier);
    }

    protected @NotNull MapPart<E> fabricate(@NotNull String key) {
        return mapPartFactory.apply(key,mapSupplier);
    }

    private record MapPartImpl<E>(String key, Supplier<Map<String, E>> mapSupplier) implements MapPart<E> {

        @Override
        public @NotNull String key() {
            return key;
        }

        @Override
        public @NotNull E get() {
            return mapSupplier.get().get(key());
        }
    }
}
