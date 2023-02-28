package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.DBMessage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
@SuppressWarnings("unused")
public interface Message extends Cloneable {

    /**
     * @return the data container of this Message
     */
    @ApiStatus.Internal
    @NotNull DBMessage getMessageData();

    /**
     * @return the miniMessage (unresolved obviously)
     * This String does contain patterns
     */
    @NotNull String getMiniMessage();

    /**
     * @return the miniMessage, but all patterns are changed to {@code <param_safe_n> & <param_unsafe_n>} (for example)
     */
    @NotNull String getMiniMessageReadyForResolving(int replacementSize);

    /**
     * @return the miniMessage, but all patterns are changed to {@code <param_safe_n> & <param_unsafe_n>} (for example)
     */
    @NotNull String getMiniMessageReadyForResolving();

    /**
     * This String will not contain patterns, use of this may be unsafe, because untrusted data may get resolved!
     *
     * @return the miniMessage (unresolved obviously)
     */
    @NotNull String getPreparedMiniMessage();

    /**
     * This Method should only be used for logging to systems that do not support MiniMessage or Components
     *
     * @return the Message formatted without colors or decoration
     * @apiNote To get a {@link MiniMessageSerializer} you should install the AdventureAPI that provides an AdventureTagManager.
     * This {@link TagManager} can be inserted here.
     * @see net.juligames.core.adventure.AdventureTagManager
     */
    @SuppressWarnings("JavadocReference")
    @NotNull String getPlainMessage(@NotNull MiniMessageSerializer serializer);

    @Deprecated
    @NotNull String getLegacyMessage(@NotNull MiniMessageSerializer serializer);

    //String getANSIMessage(); //would be cool to have - maybe one day

    /**
     * Modify the data in this message
     *
     * @param actionToPerform the operation
     */
    void doWithMiniMessage(@NotNull Function<String, String> actionToPerform);

    @NotNull Map<Integer, String> getReplacements();

    @NotNull Set<Map.Entry<Integer, String>> getReplacementSet();

    default int replacementSize() {
        return getReplacementSet().size();
    }

}
