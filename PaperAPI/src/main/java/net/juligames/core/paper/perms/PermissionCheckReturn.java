package net.juligames.core.paper.perms;

import net.juligames.core.api.API;
import net.juligames.core.api.message.MessageApi;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.api.misc.ThrowableDebug;
import net.juligames.core.paper.PaperMessageRecipient;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * @author Ture Bentzin
 * 29.01.2023
 */
public interface PermissionCheckReturn {

    String LACKING_KEY = "paper.permission.lacking";
    String ERROR_KEY = "paper.permission.lacking";

    static void registerMessages(@NotNull MessageApi messageApi) {
        messageApi.registerMessage("paper.permission.lacking", "<red>You are lacking permission: \"{0}\"!");
        messageApi.registerMessage("paper.permission.error", "Failed to check permission: \"{0}\"!");
    }

    static @NotNull Optional<MessageRecipient> recipientFromPermissible(Permissible permissible) {
        if (permissible instanceof CommandSender sender)
            return Optional.of(new PaperMessageRecipient(sender));
        return Optional.empty();
    }


    Permissible permissible();

    String permissionName();

    /**
     * Indicates if the check was completed (BUT DOES NOT INDICATE WHAT THE RESULT WAS!!!)
     *
     * @return if the check was executed without exceptions
     */
    boolean wasSuccessful();

    /**
     * Indicates if the Check was positive or negative. If wasSuccessful returns true, this will always return false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean getResult();

    /**
     * This will send the permissible the lacking permission message "paper.permission.lacking"
     */
    default void sendLackingPermissionMessageIfPossible() {
        if (!wasSuccessful()) {
            recipientFromPermissible(permissible()).ifPresent(messageRecipient ->
                    API.get().getMessageApi().sendMessage(ERROR_KEY, messageRecipient));
            API.get().getAPILogger().error("failed to check permission \"" + permissionName() + "\" for " + permissible() + " : " + exceptions());
            for (Exception exception : exceptions()) {
                ThrowableDebug.debug(exception);
            }
        }

        if (!getResult()) {
            recipientFromPermissible(permissible()).ifPresent(messageRecipient ->
                    API.get().getMessageApi().sendMessage(LACKING_KEY, messageRecipient, new String[]{permissionName()}));
        }
    }

    /**
     * This will return getResult and call sendLackingPermissionMessageIfPossible
     *
     * @return getResult
     */
    boolean checkAndContinue();

    /**
     * This will return getResult and call sendLackingPermissionMessageIfPossible and allow you to execute stuff in
     * the resultConsumer
     *
     * @return getResult
     */
    boolean checkAndContinue(BiConsumer<Permissible, Boolean> resultConsumer);

    /**
     * If wasSuccessful returns false, the exceptions that caused this are stored here!
     *
     * @return unmodifiable List
     */
    List<Exception> exceptions();
}
