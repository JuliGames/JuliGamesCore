package net.kyori.adventure.text.minimessage.internal.parser.node;

import net.kyori.adventure.text.minimessage.internal.parser.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 04.02.2023
 */
public class CheatNode extends ValueNode{
    /**
     * Creates a new element node.
     *
     * @param parent        the parent of this node
     * @param token         the token that created this node
     * @param sourceMessage the source message
     * @param value         the value of this text node
     * @since 4.10.0
     */
    public CheatNode(@Nullable ElementNode parent, @Nullable Token token, @NotNull String sourceMessage, @NotNull String value) {
        super(parent, token, sourceMessage, value);
    }

    @Override
    String valueName() {
        return "TextNode";
    }
}
