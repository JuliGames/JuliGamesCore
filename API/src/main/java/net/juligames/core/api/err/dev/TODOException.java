package net.juligames.core.api.err.dev;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class TODOException extends RuntimeException{

    @Override
    public String getMessage() {
        return "not implemented yet";
    }
}
