package net.juligames.core.api.config.mapbacked;

import org.jetbrains.annotations.ApiStatus;

/**
 * @apiNote should only be used with the {@link MapFeedInterpreter}
 * @author Ture Bentzin
 * 07.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public interface MapPart<E> {

    String key();
    E get();
}
