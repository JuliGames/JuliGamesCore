package net.juligames.core.api.minigame;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @apiNote please dont temper with this interface. The current roadmap for this is currently unclear...
 * @author Ture Bentzin
 * 09.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public
interface StartType {

    static boolean compare(@NotNull StartType s1, StartType s2) {
        return s1.isSimilar(s2);
    }

    StartType DIRECT = new SimpleStartType("DIRECT");
    StartType DELAYED = new SimpleStartType("DELAYED");

    String getName();


    record SimpleStartType(String name) implements StartType {
        @Override
        public String getName() {
            return name;
        }
    }

    default boolean isSimilar(@NotNull StartType startType) {
        return this.getName().equals(startType.getName());
    }
}
