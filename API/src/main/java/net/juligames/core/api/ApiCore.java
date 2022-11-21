package net.juligames.core.api;

import org.jetbrains.annotations.Nullable;

/**
 * @apiNote This is for distribution of the API
 * @author Ture Bentzin
 * 16.11.2022
 */
public class ApiCore {

    public static API CURRENT_API;

    @Nullable
    public static API getAPI() {
        return CURRENT_API;
    }
}
