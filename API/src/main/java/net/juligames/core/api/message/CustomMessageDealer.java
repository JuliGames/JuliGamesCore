package net.juligames.core.api.message;

import java.util.function.BiFunction;

/**
 * @author Ture Bentzin
 * 25.02.2023
 */
public interface CustomMessageDealer extends BiFunction<String,String,String> {

    /**
     * @param key the related key for context
     * @param input the given input
     * @return the input formatted as a miniMessage (may contain replacers)
     */
    @Override
    String apply(String key, String input);
}
