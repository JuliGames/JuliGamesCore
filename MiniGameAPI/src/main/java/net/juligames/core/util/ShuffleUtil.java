package net.juligames.core.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public final class ShuffleUtil {

    private static final Collector<?, ?, ?> SHUFFLER = Collectors.collectingAndThen(
            Collectors.toCollection(ArrayList::new),
            list -> {
                Collections.shuffle(list);
                return list;
            }
    );

    private ShuffleUtil() {

    }

    @SuppressWarnings("unchecked")
    public static <T> Collector<T, ?, List<T>> toShuffledList() {
        return (Collector<T, ?, List<T>>) SHUFFLER;
    }

    @Contract("_ -> param1")
    public static <T, C extends Collection<T>> @NotNull C shuffle(final @NotNull C c) {
        c.clear();
        c.addAll(List.copyOf(c).stream().collect(toShuffledList()).stream().toList());
        return c;
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> List<E> shuffleToNew(@NotNull Collection<E> collection) {
        return collection.stream().collect(toShuffledList());
    }
}
