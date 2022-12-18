package net.juligames.core.minigame.api.team;

import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
public record RTeamColor(String prefix, Tag nameDecoration, String suffix) implements TeamColor{

    @Override
    public @Nullable String prefix() {
        return prefix;
    }

    @Override
    public @NotNull Tag nameDecoration() {
        return nameDecoration;
    }

    @Override
    public @Nullable String suffix() {
        return suffix;
    }
}
