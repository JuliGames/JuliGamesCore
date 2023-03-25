package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.proxy.ProxyServer;
import de.bentzin.conversationlib.prompt.Prompt;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public interface ProxyServerPrompt extends Prompt {
    ProxyServer getProxy();
}
