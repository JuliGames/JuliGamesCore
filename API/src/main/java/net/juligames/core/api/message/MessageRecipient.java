package net.juligames.core.api.message;

import net.juligames.core.api.API;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The MessageRecipient interface defines the methods necessary for an object to receive a message.
 *
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessageRecipient {

    /**
     * Returns a human-readable name that defines this recipient.
     *
     * @return a human-readable name that defines this recipient
     */
    @NotNull String getName();

    /**
     * Delivers the specified message to this MessageRecipient. The message should always be human-readable.
     *
     * @param message the message to deliver
     */
    void deliver(@NotNull Message message);

    /**
     * Returns the locale of this MessageRecipient, or null if the locale is not specified.
     *
     * @return the locale of this MessageRecipient, or null if the locale is not specified
     */
    @Nullable String supplyLocale();

    /**
     * Returns the default locale that is distributed by the master, or the locale of this MessageRecipient if
     * specified.
     *
     * @return the default locale that is distributed by the master, or the locale of this MessageRecipient if
     * specified
     */
    default @Nullable String supplyLocaleOrDefault() {
        if (supplyLocale() != null) {
            return supplyLocale();
        }
        return API.get().getHazelDataApi().getMasterInformation().get("default_locale");
    }

    /**
     * @param miniMessage the miniMessage string to deliver
     * @deprecated Use {@link #deliver(Message)} instead.
     *
     * <p>Delivers a miniMessage string to the recipient.</p>
     */
    @Deprecated
    @ApiStatus.Internal
    void deliver(@NotNull String miniMessage);

}
