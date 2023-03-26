package net.juligames.core.paper;

import de.bentzin.conversationlib.Converser;
import de.bentzin.conversationlib.manager.ConversationManager;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public class PaperConversationManager extends ConversationManager {
    
    private static PaperConversationManager instance;

    public static PaperConversationManager getInstance() {
        return instance;
    }
    
    public PaperConversationManager(@NotNull Map<Audience, Converser> audienceConverserMap) {
        super(audienceConverserMap);
        if(instance != null) {
            throw new IllegalStateException("instance is already present!");
        }
        instance = this;
    }

    public PaperConversationManager() {
        if(instance != null) {
            throw new IllegalStateException("instance is already present!");
        }
        instance = this;
    }
}