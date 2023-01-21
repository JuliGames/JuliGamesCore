package net.juligames.core.paper.msc;

import de.bentzin.tools.console.ConsoleColors;
import net.juligames.core.api.err.dev.TODOException;
import org.bukkit.ChatColor;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public class LegacyToAnsiConverter implements Function<ChatColor,String> {
    @Override
    public String apply(ChatColor chatColor) {
        throw new TODOException();
    }
}
