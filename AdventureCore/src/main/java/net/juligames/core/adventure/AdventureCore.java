package net.juligames.core.adventure;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.adventure.api.AdventureAPI;
import net.juligames.core.adventure.api.AdventureAPICore;
import net.juligames.core.adventure.api.LegacyMessageDealer;
import net.juligames.core.api.API;
import net.juligames.core.api.message.LegacyMessageType;
import net.juligames.core.api.message.MessageApi;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 21.11.2022
 * @apiNote The AdventureCore and AdventureAPI are additional modules that are not associated with the Master
 * or the Core, they represent additional features. Because of this the AdventureCore will be started by Cores that
 * support it. Because of this the AdventureAPI will only function as intended when bundled with the API you are using!
 */
public class AdventureCore implements AdventureAPI {
    final String CORE_VERSION = "1.5-SNAPSHOT";

    private CoreAdventureTagManager adventureTagManager;
    private Logger logger;


    public void start() {
        logger = API.get().getAPILogger().adopt("adventure");
        AdventureAPICore.adventureAPI = this;

        //version check:
        final String buildVersion = API.get().getBuildVersion();
        if (!CORE_VERSION.equals(API_VERSION) || !CORE_VERSION.equals(buildVersion)) {
            //warning
            logger.warning("---------------------------------------------------------------------------------");
            logger.warning("A version mismatch was detected (please make sure that both versions are equal)!");
            logger.warning("AdventureAPI is at: " + API_VERSION);
            logger.warning("AdventureCore is at: " + CORE_VERSION);
            logger.warning("Core is at: " + buildVersion);
            logger.warning("You will not receive support for this unsafe combination!");
            logger.warning("---------------------------------------------------------------------------------");
        }

        //init
        adventureTagManager = new CoreAdventureTagManager();

        //default the replacementTypes:
        JDBITagAdapter.ReplacementType.defaultToJDBI(API.get().getSQLManager().getJdbi());
        logger.info("loading replacements from jdbi...");
        adventureTagManager.load();
        logger.info("loaded replacements from jdbi...");

        logger.info("adventureAPI is live!");
    }

    @ApiStatus.Internal
    public void dropApiService() {
        logger.info("dropping adventure api...");
        AdventureAPICore.adventureAPI = null;
    }

    @Override
    public @NotNull CoreAdventureTagManager getAdventureTagManager() {
        return adventureTagManager;
    }

    @Override
    public void registerLegacyMessage(@NotNull MessageApi messageApi, @NotNull String key, @NotNull String input,
                                      @NotNull LegacyMessageType legacyMessageType) {
        LegacyMessageDealer legacyMessageDealer = new LegacyMessageDealer(legacyMessageType);
        messageApi.registerThirdPartyMessage(key, input, legacyMessageDealer);
    }

    @Override
    public void registerLegacyMessage(@NotNull String key, @NotNull String input,
                                      @NotNull LegacyMessageType legacyMessageType) {
        registerLegacyMessage(API.get().getMessageApi(), key, input, legacyMessageType);
    }

    @Override
    public @NotNull Component forAudience(@NotNull String messageKey, @NotNull Audience audience, String... replacements) {
        return getAdventureTagManager().resolve(API.get().getMessageApi().
                getMessageSmart(messageKey, audience.get(Identity.LOCALE).orElseThrow(), replacements));
    }

    @Override
    public @NotNull Component forAudience(@NotNull String messageKey, @NotNull Audience audience) {
        return getAdventureTagManager().resolve(API.get().getMessageApi().
                getMessageSmart(messageKey, audience.get(Identity.LOCALE).orElseThrow()));
    }

    @ApiStatus.Internal
    public Logger logger() {
        return logger;
    }
}
