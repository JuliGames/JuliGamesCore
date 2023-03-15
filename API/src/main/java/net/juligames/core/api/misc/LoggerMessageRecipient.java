package net.juligames.core.api.misc;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.api.message.MiniMessageSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 15.03.2023
 */
public record LoggerMessageRecipient(Logger logger, @Nullable MiniMessageSerializer miniMessageSerializer) implements MessageRecipient {
    @Override
    public @NotNull String getName() {
        return logger.getFullName();
    }

    @Override
    public void deliver(@NotNull Message message) {
        if(miniMessageSerializer != null)
         logger.info(message.getPlainMessage(miniMessageSerializer));
        else logger.info("UNPARSED: " + message.getPreparedMiniMessage());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void deliver(@NotNull String miniMessage) {
        if(miniMessageSerializer != null)
            logger.info(miniMessageSerializer.resolvePlain(miniMessage));
        else logger.info("UNPARSED: " + miniMessage);
    }

    @Override
    public @Nullable String supplyLocale() {
        return null;
    }
}
