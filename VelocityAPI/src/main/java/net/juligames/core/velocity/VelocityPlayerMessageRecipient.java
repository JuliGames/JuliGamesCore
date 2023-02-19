package net.juligames.core.velocity;

import com.velocitypowered.api.proxy.Player;
import net.juligames.core.adventure.api.AudienceMessageRecipient;

import java.util.Objects;

/**
 * @author Ture Bentzin
 * 03.12.2022
 * @apiNote This is used for messaging players. use {@link ProxyMessageRecipient} to message the proxy and {@link ServerMessageRecipient} to message a specific server
 */
@SuppressWarnings("deprecation")
public class VelocityPlayerMessageRecipient extends AudienceMessageRecipient {
    public VelocityPlayerMessageRecipient(Player player) {
        super(player.getUsername(), () -> Objects.requireNonNull(player.getEffectiveLocale()).toString(), player); //In the case that this should throw NullPointerException when player is not ready for message - contact developer
    }
}
