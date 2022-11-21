package net.juligames.core.adventure.api;

import net.juligames.core.adventure.AdventureTagManager;
import net.juligames.core.api.err.APIException;
import org.jetbrains.annotations.NotNull;

/**
 @apiNote The AdventureCore and AdventureAPI are additional modules that are not associated with the Master
  or the Core, they represent additional features. Because of this the AdventureCore will be started by Cores that
 support it. Because of this the AdventureAPI will only function as intended when bundled with the API you are using!
 Please make sure the modules (AdventureAPI & AdventureCore) are the same version to avoid issues while execution!
 * @author Ture Bentzin
 * 21.11.2022
 */
public interface AdventureAPI {
    String API_VERSION = "1.0-SNAPSHOT";

    static @NotNull AdventureAPI get() {
        AdventureAPI api = AdventureAPICore.getAPI();
        if(api != null)
            return api;
        else throw new APIException();
    }

    AdventureTagManager getAdventureTagManager();

}
