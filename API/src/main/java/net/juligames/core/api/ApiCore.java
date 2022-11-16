package net.juligames.core.api;

/**
 * @apiNote This is for distribution of the API
 * @author Ture Bentzin
 * 16.11.2022
 */
public class ApiCore {

    public static API CURRENT_API;

    public static API getAPI() {
        return CURRENT_API;
    }
}
