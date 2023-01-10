package net.juligames.core.api.config;

import java.util.Collection;

/**
 * @author Ture Bentzin
 * 10.01.2023
 */
public class SplittingCollectionInterpreter<T> implements IterableInterpreter<T, Collection<T>> {
    @Override
    public Collection<T> interpret(String input) throws Exception {
        return null;
    }

    @Override
    public String reverse(Collection<T> ts) {
        return null;
    }
}
