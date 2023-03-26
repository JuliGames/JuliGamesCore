package net.juligames.core.api;

/**
 * @author Ture Bentzin
 * 20.11.2022
 * @apiNote Methods annotated with this might throw a {@link net.juligames.core.api.err.dev.TODOException}
 * @suppression may suppress convert to record and unused
 */
@SuppressWarnings("JavadocDeclaration")
public @interface TODO {
    boolean doNotcall() default false;
}
