package net.juligames.core.adventure.api;

import net.juligames.core.adventure.AdventureTagManager;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MiniMessageSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public class MessageRepresentation implements ComponentLike {



    private final @NotNull AdventureTagManager adventureTagManager;
    private final @NotNull Message message;

    public MessageRepresentation(@NotNull AdventureTagManager adventureTagManager, @NotNull Message message){
        this.adventureTagManager = adventureTagManager;
        this.message = message;
    }

    public MessageRepresentation(@NotNull Message message){
        this.adventureTagManager = AdventureAPI.get().getAdventureTagManager();
        this.message = message;
    }

    @Override
    public @NotNull Component asComponent() {
        return adventureTagManager.resolve(message);
    }

    public Message getMessage() {
        return message;
    }
}
