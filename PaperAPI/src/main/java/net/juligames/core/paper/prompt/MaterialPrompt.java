package net.juligames.core.paper.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import org.bukkit.Material;
import org.bukkit.map.MapPalette;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class MaterialPrompt extends ValidatingPrompt {
    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return Material.getMaterial(input) != null;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        MapPalette.matchColor()
        return acceptMaterialInput(context, Objects.requireNonNull(Material.getMaterial(input)));
    }

    public abstract Prompt acceptMaterialInput(@NotNull ConversationContext context, @NotNull Material material);

}
