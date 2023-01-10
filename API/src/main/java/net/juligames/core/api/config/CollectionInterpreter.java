package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The {@link CollectionInterpreter} can be used to semi-human-readable enter stuff into the configuration
 * @author Ture Bentzin 27.11.2022
 * @author Bommels05
 * @see SlimCollectionInterpreter
 * @see IterableInterpreter
 */
@SuppressWarnings("unused")
public class CollectionInterpreter<T> implements IterableInterpreter<T,Collection<T>> {

    private final Interpreter<T> tInterpreter;

    public CollectionInterpreter(Interpreter<T> tInterpreter) {
        this.tInterpreter = tInterpreter;
    }

    @Override
    public Collection<T> interpret(final @NotNull String input) throws Exception {
        if (!input.startsWith("{") || !input.endsWith("}")) {
            throw new IllegalArgumentException("Input has to start with { and end with }");
        }
        if (input.length() == 2) {
            return new ArrayList<>();
        }

        String converted = input.replaceFirst("\\{", "");

        List<T> output = new ArrayList<>();

        while (!converted.startsWith("}")) {
            if (!converted.startsWith(":length=")) {
                throw new IllegalArgumentException("Expected length");
            }
            checkLength(converted);

            converted = converted.replaceFirst(":length=", "");
            String number = converted.substring(0, converted.indexOf(":"));
            int length;
            try {
                length = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Length not valid");
            }
            converted = converted.replaceFirst(number, "");
            converted = converted.replaceFirst(":", "");

            if (converted.charAt(length) != ':') {
                throw new IllegalArgumentException("Expected end of value");
            }
            String value = converted.substring(0, length);
            output.add(tInterpreter.interpret(value));

            converted = converted.substring(length + 1);
        }

        return output;
    }

    private void checkLength(@NotNull String input) {
        if (input.length() == 0) {
            throw new IllegalArgumentException("Unexpected end of Sequence");
        }
    }


    @Override
    public String reverse(@NotNull Collection<T> ts) {
        StringBuilder builder = new StringBuilder();

        for (T value : ts) {
            String converted = tInterpreter.reverse(value);
            builder.append(":length=").append(converted.length()).append(":").append(converted).append(":");
        }

        return "{" + builder + "}";
    }

    private @NotNull StringBuilder appendObject(T object) {
        StringBuilder appender = new StringBuilder();
        appender.append("[").append(tInterpreter.reverse(object)).append("]");
        return appender;
    }

    public Interpreter<T> tInterpreter() {
        return tInterpreter;
    }
}