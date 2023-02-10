package net.juligames.core.api.message;

import org.jetbrains.annotations.Range;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 10.02.2023
 */
public interface Replacement {

    @Range(from = 0, to = Integer.MAX_VALUE)
    int getIndex();

    Type getType();

    Function<String, String> insertInstant();

    String getInsertion();

    String getAsPattern();

    enum Type {
        /**
         * The Insertion will be handed as MiniMessage
         */
        TRUSTED,

        /**
         * The Insertion will be handled as Text
         */
        UNTRUSTED,

        /**
         * The Replacement will insert itself as a Pattern
         */
        IGNORE;
    }
}
