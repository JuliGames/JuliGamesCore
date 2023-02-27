package net.juligames.core.adventure.api;

import net.juligames.core.api.message.CustomMessageDealer;
import net.juligames.core.api.message.LegacyMessageType;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.02.2023
 */
 @ApiStatus.Experimental
@ApiStatus.AvailableSince("1.4")
public record LegacyMessageDealer(@NotNull LegacyMessageType legacyMessageType) implements CustomMessageDealer {

    @Override
    public @NotNull String apply(String key, String input) {
        if(legacyMessageType().equals(LegacyMessageType.AMPERSAND)) {
            return AdventureAPI.get().getAdventureTagManager().translateLegacyToMiniMessage(input);
        }else if (legacyMessageType().equals(LegacyMessageType.SECTION)) {
            return AdventureAPI.get().getAdventureTagManager().translateLegacySectionToMiniMessage(input);
        }else if (legacyMessageType() instanceof LegacyMessageType.CustomLegacyMessageType) {
            return customToMiniMessage(input, legacyMessageType().getChar());
        }else {
            throw new IllegalArgumentException("unexpected value: " + legacyMessageType());
        }
    }

    private @NotNull String customToMiniMessage(String custom, char c) {
        return AdventureAPI.get().getAdventureTagManager().
                fromComponent(LegacyComponentSerializer.legacy(c).deserialize(custom));
    }
}
