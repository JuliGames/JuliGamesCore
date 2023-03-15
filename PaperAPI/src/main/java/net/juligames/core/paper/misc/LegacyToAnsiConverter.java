package net.juligames.core.paper.misc;

import net.juligames.core.api.err.dev.TODOException;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public class LegacyToAnsiConverter implements Function<ChatColor, String> {
    @Override
    public @NotNull String apply(@NotNull ChatColor chatColor) {
        throw new TODOException();
    }
}
