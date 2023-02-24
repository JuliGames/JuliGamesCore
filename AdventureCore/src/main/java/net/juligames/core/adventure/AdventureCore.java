package net.juligames.core.adventure;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.adventure.api.AdventureAPI;
import net.juligames.core.adventure.api.AdventureAPICore;
import net.juligames.core.api.API;
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
    final String CORE_VERSION = "1.4";

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

    @ApiStatus.Internal
    public Logger logger() {
        return logger;
    }
}
