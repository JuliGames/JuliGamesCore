package net.juligames.core.paper.misc.configmapping;

import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 14.04.2023
 */
@ApiStatus.AvailableSince("1.6")
public @interface Autofill {
    String defaultName = "";

    String customName() default defaultName;
}
