package net.juligames.core.api.err;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote This is used to indicate that someone tried to call something that is not provided
 */
public final class APIException extends RuntimeException {

    private String customMessage = "You cant run the api without a core!";

    public APIException() {
    }

    public APIException(String customMessage) {
        this.customMessage = customMessage;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }
}
