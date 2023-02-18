package net.juligames.core.adventure.api;

import de.bentzin.tools.Hardcode;
import net.juligames.core.api.API;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 03.12.2022
 * @apiNote This may be used as a foundation to other audience based message recipients!
 */
public class AudienceMessageRecipient implements MessageRecipient {

    public static Supplier<String> defaultLocaleSupplier = () -> API.get().getHazelDataApi().getMasterInformation().get("default_locale");

    private final String name;
    private final Supplier<String> locale;
    private final Audience audience;

    public AudienceMessageRecipient(String name, Supplier<String> locale, Audience audience) {
        this.name = name;
        this.locale = locale;
        this.audience = audience;
    }

    @Contract("_ -> new")
    @ApiStatus.Experimental
    public static @NotNull AudienceMessageRecipient getByPointer(@NotNull Audience audience) {
        String ls = API.get().getHazelDataApi().getMasterInformation().get("default_locale");
        Locale locale = (audience.pointers().get(Identity.LOCALE).orElse(null));
        if (locale != null) {
            ls = locale.toString();
        }
        String finalLs = ls; //for integrity
        return new AudienceMessageRecipient(audience.pointers().get(Identity.NAME).orElse("unknownAudience"),
                () -> finalLs, audience);
    }

    /**
     * @return "EN"
     * @deprecated use {@link #defaultLocale()} instead
     */
    @Contract(pure = true)
    @Hardcode
    @Deprecated(forRemoval = true)
    public static @NotNull Supplier<String> english() {
        return () -> "EN";
    }

    public static @NotNull Supplier<String> defaultLocale() {
        return defaultLocaleSupplier;
    }

    /**
     * @return A human-readable name that defines this recipient
     */
    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void deliver(@NotNull Message message) {
        Component component = AdventureAPI.get().getAdventureTagManager().resolve(message);
        audience.sendMessage(component);
    }

    /**
     * delivers a miniMessage string to the recipient
     *
     * @param miniMessage
     */
    @Override
    public void deliver(@NotNull String miniMessage) {
        Component component = AdventureAPI.get().getAdventureTagManager().resolve(miniMessage);
        audience.sendMessage(component);
    }

    @Override
    public @Nullable String supplyLocale() {
        return locale.get();
    }
}
