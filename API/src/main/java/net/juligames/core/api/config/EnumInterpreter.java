package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Here is an exmaple on how to use the {@link EnumInterpreter} with the fictional enum "Color"
 * <pre>
 * {@code
 * public enum Color {
 *     RED,
 *     GREEN,
 *     BLUE
 * }
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         EnumInterpreter<Color> colorInterpreter = new EnumInterpreter<>(Color.class);
 *
 *         // Reverse an enum value to a string
 *         Color color = Color.RED;
 *         String reversed = colorInterpreter.reverse(color);
 *         System.out.println("Reversed: " + reversed); // Output: Reversed: RED
 *
 *         // Interpret a string as an enum value
 *         String input = "GREEN";
 *         Color interpreted = colorInterpreter.interpret(input);
 *         System.out.println("Interpreted: " + interpreted); // Output: Interpreted: GREEN
 *     }
 * }
 * }
 * </pre>
 *
 * @author Ture Bentzin
 * 15.03.2023
 */
@ApiStatus.AvailableSince("1.5")
@ApiStatus.Experimental
public class EnumInterpreter<T extends Enum<T>> implements Interpreter<T> {

    private final Class<T> enumClazz;

    /**
     * Constructs a new {@link EnumInterpreter} instance for the specified enum class.
     *
     * @param enumClazz the class object for the enum type
     */
    public EnumInterpreter(Class<T> enumClazz) {
        this.enumClazz = enumClazz;
    }

    /**
     * Interprets the input string as an enum value of type T.
     *
     * @param input the input string to be interpreted
     * @return the interpreted enum value
     * @throws IllegalArgumentException if the input string is not a valid name of an enum constant belonging to type T
     */
    @Override
    public @NotNull T interpret(@NotNull String input) throws IllegalArgumentException {
        return T.valueOf(enumClazz, input);
    }

    /**
     * Returns the name of the specified enum value.
     *
     * @param t the enum value to be reversed
     * @return the name of the enum value
     */
    @Override
    public @NotNull String reverse(@NotNull T t) {
        return t.name();
    }

    /**
     * Returns the class object for the enum type.
     *
     * @return the class object for the enum type
     */
    public Class<T> getEnumClazz() {
        return enumClazz;
    }
}


