package net.juligames.core.adventure.api.prompt;

import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class NamedTextColorPrompt extends IndexBackedPrompt<NamedTextColor> {
    public NamedTextColorPrompt() {
        super(NamedTextColor.NAMES);
    }

}
