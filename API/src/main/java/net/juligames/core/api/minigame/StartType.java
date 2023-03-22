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

    StartType DIRECT = new SimpleStartType("DIRECT");
    StartType DELAYED = new SimpleStartType("DELAYED");

    static boolean compare(@NotNull StartType s1, StartType s2) {
        return s1.isSimilar(s2);
    }

    String getName();

    default boolean isSimilar(@NotNull StartType startType) {
        return this.getName().equals(startType.getName());
    }

    record SimpleStartType(String name) implements StartType {
        @Override
        public String getName() {
            return name;
        }
    }
}
