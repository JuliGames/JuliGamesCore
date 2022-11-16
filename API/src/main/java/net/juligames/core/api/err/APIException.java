package net.juligames.core.api.err;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class APIException extends RuntimeException{
    @Override
    public String getMessage() {
        return "You cant run the api without a core!";
    }
}
