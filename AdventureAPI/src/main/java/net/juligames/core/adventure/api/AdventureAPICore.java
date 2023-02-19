package net.juligames.core.adventure.api;

import net.juligames.core.api.err.APIException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 21.11.2022
 */
public class AdventureAPICore {


    public static @Nullable AdventureAPI adventureAPI;

    public static @NotNull AdventureAPI getAPI() {
        if (adventureAPI == null) {
            throw new APIException();
        }
        return adventureAPI;
    }
}
