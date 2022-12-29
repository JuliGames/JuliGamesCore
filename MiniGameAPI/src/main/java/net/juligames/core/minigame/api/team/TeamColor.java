package net.juligames.core.minigame.api.team;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
public interface TeamColor {

    @Contract("_ -> new")
    static @NotNull TeamColor fromTextColor(TextColor textColor) {
        return new RTeamColor("", Tag.styling(builder -> builder.color(textColor)), "");
    }

    @Contract("_ -> new")
    static @NotNull TeamColor fromNamedTextColor(@NotNull NamedTextColor namedTextColor) {
        //TODO: fix prefix...
        Tag colorTag = Tag.styling(builder -> builder.color(namedTextColor));
        TextComponent color = Component.text().color(namedTextColor).build();
        String miniMessage = MiniMessage.miniMessage().serialize(color);
        return new RTeamColor(namedTextColor.examinableName(), colorTag, "");
    }

    @Contract(" -> new")
    static @NotNull TeamColor getDefaultWhite() {
        return new RTeamColor("", Tag.styling(b -> b.color(NamedTextColor.WHITE)), "");
    }

    static @NotNull Set<TeamColor> getAllDefaultNamed() {
        Set<TeamColor> teamColors = new HashSet<>();
        for (NamedTextColor value : NamedTextColor.NAMES.values()) {
            teamColors.add(fromNamedTextColor(value));
        }
        return teamColors;
    }


    /**
     * MiniMessage displayed in front
     */
    @Nullable
    String prefix();


    /**
     * Tag used to decorate the center
     */
    @NotNull
    Tag nameDecoration();

    /**
     * MiniMessage displayed after
     */
    @Nullable
    String suffix();
}
