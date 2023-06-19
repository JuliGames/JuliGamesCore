package net.juligames.core.paper.prompt;

import de.bentzin.conversationlib.prompt.Prompt;
import org.bukkit.Server;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public interface ServerPrompt extends Prompt {

    Server getServer();
}
