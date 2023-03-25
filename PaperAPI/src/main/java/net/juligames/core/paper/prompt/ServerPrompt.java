package net.juligames.core.paper.prompt;

import de.bentzin.conversationlib.prompt.Prompt;
import org.bukkit.Server;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public interface ServerPrompt extends Prompt {

    Server getServer();
}
