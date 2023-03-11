package net.juligames.core.adventure.api;

import net.juligames.core.adventure.AdventureTagManager;
import net.juligames.core.api.err.APIException;
import net.juligames.core.api.message.LegacyMessageType;
import net.juligames.core.api.message.MessageApi;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 21.11.2022
 * @apiNote The AdventureCore and AdventureAPI are additional modules that are not associated with the Master
 * or the Core, they represent additional features. Because of this the AdventureCore will be started by Cores that
 * support it. Because of this the AdventureAPI will only function as intended when bundled with the API you are using!
 * Please make sure the modules (AdventureAPI & AdventureCore) are the same version to avoid issues while execution!
 */
public interface AdventureAPI {
    @NotNull String API_VERSION = "1.5-SNAPSHOT";

    static @NotNull AdventureAPI get() {
        AdventureAPI api = AdventureAPICore.getAPI();
        if (api != null)
            return api;
        else throw new APIException();
    }

    @NotNull AdventureTagManager getAdventureTagManager();

    @Deprecated
    @ApiStatus.AvailableSince("1.4")
    void registerLegacyMessage(@NotNull MessageApi messageApi, @NotNull String key, @NotNull String input, @NotNull LegacyMessageType legacyMessageType);

    @Deprecated
    @ApiStatus.AvailableSince("1.4")
    void registerLegacyMessage(@NotNull String key, @NotNull String input, @NotNull LegacyMessageType legacyMessageType);

    /**
     * This Method is a shortcut for the MessageAPI.
     * It supplies the best message for the given key and locale (through audience)
     * This is default implementation:
     * {@code         return getAdventureTagManager().resolve(API.get().getMessageApi().
     * getMessageSmart(messageKey,audience.get(Identity.LOCALE).orElseThrow(), replacements));}
     */
    @ApiStatus.AvailableSince("1.5")
    @NotNull Component forAudience(@NotNull String messageKey, @NotNull Audience audience, String... replacements);


    /**
     * This Method is a shortcut for the MessageAPI.
     * It supplies the best message for the given key and locale (through audience)
     * This is default implementation:
     * {@code         return getAdventureTagManager().resolve(API.get().getMessageApi().
     * getMessageSmart(messageKey,audience.get(Identity.LOCALE).orElseThrow()));}
     */
    @ApiStatus.AvailableSince("1.5")
    @NotNull Component forAudience(@NotNull String messageKey, @NotNull Audience audience);

}
