package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.proxy.ProxyServer;
import de.bentzin.conversationlib.prompt.Prompt;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public interface ProxyServerPrompt extends Prompt {
    ProxyServer getProxy();
}
