package net.juligames.core.velocity;

import de.bentzin.conversationlib.Converser;
import de.bentzin.conversationlib.manager.ConversationManager;
import net.juligames.core.api.TODO;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Ture Bentzin
 * 25.03.2023
 * @apiNote This currently does not support automatic input insertion!
 */
@TODO
public class VelocityConversationManager extends ConversationManager {

    private static VelocityConversationManager instance;

    public VelocityConversationManager(@NotNull Map<Audience, Converser> audienceConverserMap) {
        super(audienceConverserMap);
        if (instance != null) {
            throw new IllegalStateException("instance is already present!");
        }
        instance = this;
    }

    public VelocityConversationManager() {
        if (instance != null) {
            throw new IllegalStateException("instance is already present!");
        }
        instance = this;
    }

    public static VelocityConversationManager getInstance() {
        return instance;
    }
}
