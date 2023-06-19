package net.juligames.core.api.minigame;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 09.03.2023
 * @apiNote please don't temper with this interface. The current roadmap for this is currently unclear...
 */
@ApiStatus.AvailableSince("1.5")
public interface StartType {

    /**
     * The direct starting option.
     */
    StartType DIRECT = new SimpleStartType("DIRECT");

    /**
     * The delayed starting option.
     */
    StartType DELAYED = new SimpleStartType("DELAYED");

    /**
     * Compares two StartType objects for similarity.
     *
     * @param s1 the first StartType object
     * @param s2 the second StartType object
     * @return true if the objects are similar, false otherwise
     */
    static boolean compare(@NotNull StartType s1, StartType s2) {
        return s1.isSimilar(s2);
    }

    /**
     * Returns the name of this StartType object.
     *
     * @return the name of this StartType object
     */
    String getName();

    /**
     * Checks whether this StartType object is similar to another StartType object.
     *
     * <p>
     * Two StartType objects are considered similar if they have the same name.
     * </p>
     *
     * @param startType the StartType object to compare to
     * @return true if the objects are similar, false otherwise
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * StartType s1 = StartType.DIRECT;
     * StartType s2 = new SimpleStartType("DIRECT");
     * boolean areSimilar = s1.isSimilar(s2); // true
     *
     * StartType s3 = new ImaginaryStartType("IMAGINARY");
     * StartType s4 = new SimpleStartType("IMAGINARY");
     * boolean areSimilar2 = s3.isSimilar(s4); // true
     * }</pre>
     */
    default boolean isSimilar(@NotNull StartType startType) {
        return this.getName().equals(startType.getName());
    }


    /**
     * A simple implementation of the StartType interface.
     */
    record SimpleStartType(String name) implements StartType {
        @Override
        public String getName() {
            return name;
        }
    }
}
