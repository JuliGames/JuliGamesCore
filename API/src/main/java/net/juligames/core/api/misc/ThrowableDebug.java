package net.juligames.core.api.misc;

import net.juligames.core.api.API;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Ture Bentzin
 * 29.01.2023
 */
public final class ThrowableDebug {

    private ThrowableDebug(){}

    @ApiStatus.Experimental
    public static void debug(@NotNull Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        API.get().getAPILogger().debug(throwable + ":");
        API.get().getAPILogger().debug(writer.toString());
    }
}
