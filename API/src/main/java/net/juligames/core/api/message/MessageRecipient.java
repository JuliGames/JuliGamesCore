package net.juligames.core.api.message;

import net.juligames.core.api.API;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessageRecipient {

    /**
     * @return A human-readable name that defines this recipient
     */
    String getName();

    /**
     * Devlivers the specified Message to this MessageRecipient. The message should always be human-readable!
     * @param message the message to deliver
     */
    void deliver(Message message);


    /**
     * delivers a miniMessage string to the recipient
     *
     */
    @ApiStatus.Internal
    @Deprecated
    void deliver(String miniMessage);

    @Nullable
    String supplyLocale();

    default String supplyLocaleOrDefault(){
        if(supplyLocale() != null) return supplyLocale();
        return API.get().getHazelDataApi().getMasterInformation().get("default_locale");
    }
}
