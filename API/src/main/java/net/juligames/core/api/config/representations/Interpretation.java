package net.juligames.core.api.config.representations;

import net.juligames.core.api.config.PrimitiveInterpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 17.04.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class Interpretation<T> implements Representation<T> {

    public final @NotNull PrimitiveInterpreter<T> interpreter;

    public Interpretation(@NotNull PrimitiveInterpreter<T> interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public final @NotNull T represent() {
        try {
            return interpreter.interpret(getRepresentation());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String getRepresentation();
}
