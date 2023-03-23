package net.juligames.core.api.misc;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 03.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class ObjectHashtableToStringHashtableMapper
        implements Function<Hashtable<Object, Object>, Hashtable<String, String>> {

    @Override
    public Hashtable<String, String> apply(@NotNull Hashtable<Object, Object> objectObjectHashtable) {
        final Hashtable<String, String> stringStringHashtable = new Hashtable<>();
        objectObjectHashtable.forEach((key, value) -> stringStringHashtable.put(key.toString(), value.toString()));
        return stringStringHashtable;
    }
}
