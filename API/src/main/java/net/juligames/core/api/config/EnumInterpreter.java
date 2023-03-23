package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 15.03.2023
 */
@ApiStatus.AvailableSince("1.5")
@ApiStatus.Experimental
public class EnumInterpreter<T extends Enum<T>> implements Interpreter<T> {

    private final Class<T> enumClazz;

    public EnumInterpreter(Class<T> enumClazz) {
        this.enumClazz = enumClazz;
    }

    @Override
    public @NotNull T interpret(@NotNull String input) throws Exception {
        return T.valueOf(enumClazz, input);
    }

    @Override
    public @NotNull String reverse(@NotNull T t) {
        return t.name();
    }

    public Class<T> getEnumClazz() {
        return enumClazz;
    }
}
