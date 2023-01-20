package net.juligames.core.adventure.api;

import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 03.12.2022
 * @apiNote This may be used as a foundation to other audience based message recipients!
 */
public class AudienceMessageRecipient implements MessageRecipient {

    private final String name;
    private final Supplier<String> locale;
    private final Audience audience;

    public AudienceMessageRecipient(String name, Supplier<String> locale, Audience audience) {
        this.name = name;
        this.locale = locale;
        this.audience = audience;
    }

    @Contract(pure = true)
    public static @NotNull Supplier<String> english() {
        return () -> "EN";
    }

    /**
     * @return A human-readable name that defines this recipient
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void deliver(Message message) {
        Component component = AdventureAPI.get().getAdventureTagManager().resolve(message);
        audience.sendMessage(component);
    }

    /**
     * delivers a miniMessage string to the recipient
     *
     * @param miniMessage
     */
    @Override
    public void deliver(String miniMessage) {
        Component component = AdventureAPI.get().getAdventureTagManager().resolve(miniMessage);
        audience.sendMessage(component);
    }

    @Override
    public @Nullable String supplyLocale() {
        return locale.get();
    }
}
