package net.juligames.core.api.config.property;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * The {@link PropertyInterpreter} allows to store a reference to a property that is set. The value that is read will
 * be used as the "key" for getting the property. If you {@link #reverse(Object)} then {@link String#valueOf(Object)}
 * will be used
 *
 * @author Ture Bentzin
 * 07.03.2023
 */
@SuppressWarnings("unused")
@ApiStatus.AvailableSince("1.5")
public final class PropertyInterpreter<E> implements Interpreter<E> {

    private final @NotNull Function<String, E> getter;

    private PropertyInterpreter(@NotNull Function<String, E> getter) {
        this.getter = getter;
    }

    /**
     * @param tInterpreter WARNING: the tInterpreter#reverse method is NOT USED!!! {@link String#valueOf(T)} is used instead!!
     * @param <T>          the Type
     * @return T
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull <T> PropertyInterpreter<T> customPropertyInterpreter(Interpreter<T> tInterpreter) {
        return new PropertyInterpreter<>(s -> {
            try {
                return tInterpreter.interpret(System.getProperty(s));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @param tInterpreter is used with the result of {@link System#getProperty(String)} for the given input
     * @param <T>          the Type
     * @return T
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull <T> PropertyInterpreter<T> customPropertyInterpreter(Function<String, T> tInterpreter) {
        return new PropertyInterpreter<>(s -> {
            try {
                return tInterpreter.apply(System.getProperty(s));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<String> stringPropertyInterpreter() {
        return new PropertyInterpreter<>(System::getProperty);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<CharSequence> charSequencePropertyInterpreter() {
        return new PropertyInterpreter<>(System::getProperty);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Boolean> booleanPropertyInterpreter() {
        return new PropertyInterpreter<>(Boolean::getBoolean);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Integer> integerPropertyInterpreter() {
        return new PropertyInterpreter<>(Integer::getInteger);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Double> doublePropertyInterpreter() {
        return new PropertyInterpreter<>(s -> Double.parseDouble(System.getProperty(s)));
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Float> floatPropertyInterpreter() {
        return new PropertyInterpreter<>(s -> Float.parseFloat(System.getProperty(s)));
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Short> shortPropertyInterpreter() {
        return new PropertyInterpreter<>(s -> Short.parseShort(System.getProperty(s)));
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Byte> bytePropertyInterpreter() {
        return new PropertyInterpreter<>(s -> Byte.parseByte(System.getProperty(s)));
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Character> charPropertyInterpreter() {
        return new PropertyInterpreter<>(s -> System.getProperty(s).charAt(0));
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull PropertyInterpreter<Long> longPropertyInterpreter() {
        return new PropertyInterpreter<>(Long::getLong);
    }

    @Override
    public @NotNull E interpret(@NotNull String input) throws Exception {
        return getter.apply(input);
    }

    @Override
    public @NotNull String reverse(@NotNull E e) {
        return String.valueOf(e);
    }

    public @NotNull Function<String, E> getGetter() {
        return getter;
    }
}
