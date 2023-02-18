package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Ture Bentzin 27.11.2022
 * @author DSeeLP
 * @deprecated use {@link ConfigurationAPI#splitToWriter(Collection, Interpreter)} and similar instead
 */
@SuppressWarnings("DefaultAnnotationParam")
@Deprecated(forRemoval = false)
public class SlimCollectionInterpreter<T> implements IterableInterpreter<T, Collection<T>> {

    private final Interpreter<T> tInterpreter;

    public SlimCollectionInterpreter(Interpreter<T> tInterpreter) {
        this.tInterpreter = tInterpreter;
    }

    @Override
    public @NotNull List<T> interpret(final @NotNull String input) throws Exception {
        if (input.isEmpty()) {
            return Collections.emptyList();
        }
        @SuppressWarnings("UnusedAssignment") int cLength = -1;
        String inp = input;
        {
            TextLength textLength = readLength(inp);
            if (textLength == null) {
                throw new IllegalArgumentException("Unable to find text Length");
            }
            if (textLength.length != inp.length() - textLength.used) {
                throw new IllegalArgumentException("Decoded length doesn't match text length");
            }
            inp = inp.substring(textLength.used, textLength.length + textLength.used);
            TextLength collectionLength = readLength(inp);
            if (collectionLength == null) {
                throw new IllegalArgumentException("Unable to find collection Length");
            }
            cLength = collectionLength.length;
            inp = inp.substring(textLength.used - 1);
        }

        List<T> list = new ArrayList<>();

        while (!inp.isEmpty()) {
            TextLength length = readLength(inp);
            if (length == null) {
                throw new IllegalArgumentException("length not found... fuck");
            }
            String text = inp.substring(length.used, length.length + length.used);
            inp = inp.substring(text.length() + length.used);
            list.add(tInterpreter.interpret(text));
        }

        if (cLength != list.size()) {
            throw new IllegalStateException("Encoded length doesn't match actual length");
        }

        return list;
    }

    private @Nullable TextLength readLength(@NotNull String input) throws NumberFormatException {
        if (input.isEmpty()) {
            return null;
        }

        char c = input.charAt(0);
        var num_length = Integer.parseUnsignedInt("" + c);
        if (num_length == 0) {
            return null;
        }
        int len = 1 + num_length;
        int actualLength = input.length();
        if (actualLength <= len) {
            return null;
        }
        String text = input.substring(1, num_length + 1);
        int length = Integer.parseUnsignedInt(text);
        return new TextLength(length, text.length() + 1);
    }

    private @NotNull String encodeLength(int length) {
        String text = Integer.toString(length);
        return "" + text.length() + "" + text;
    }

    @Override
    public @NotNull String reverse(@NotNull Collection<T> ts) {
        StringBuilder builder = new StringBuilder();
        String collectionLength = encodeLength(ts.size());
        StringJoiner innerJoiner = new StringJoiner("");
        ts.forEach(t -> innerJoiner.add(appendObject(t)));
        builder.append(encodeLength(innerJoiner.length() + collectionLength.length()));
        builder.append(collectionLength);
        builder.append(innerJoiner);
        return builder.toString();
    }

    private @NotNull String appendObject(T object) {
        String text = tInterpreter.reverse(object);
        String length = encodeLength(text.length());
        return length + text;
    }

    public Interpreter<T> tInterpreter() {
        return tInterpreter;
    }

    private record TextLength(int length, int used) {
    }
}