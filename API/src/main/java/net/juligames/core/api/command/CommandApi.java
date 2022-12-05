package net.juligames.core.api.command;
import org.checkerframework.checker.optional.qual.MaybePresent;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Ture Bentzin
 * 05.12.2022
 */
public interface CommandApi {

    /**
     * Execute a Command for the associated CommandHandling on the target instance. This is an unchecked operation!
     * @param command the command to execute
     * @param targets the targets
     */
    void sendCommand(String command, UUID... targets);

    /**
     * Executes a Command on every instance that matches the given Predicate
     * @param command the command to execute
     * @param targetIdentifier the predicate to identify the target
     */
    void sendCommand(String command, Predicate<UUID> targetIdentifier);

    /**
     * Not every Command will behave the same everywhere
     */
    @Deprecated
    void broadcastCommand(String command);

    /**
     * @apiNote if the optional is empty then the current instance does not support command execution
     * @return the consumer wich is called when a command should be executed on this instance
     */
    @MaybePresent
    Optional<Consumer<String>> getCommandHandler();

    /**
     *
     * @return true if getCommandHandler is present
     * @see CommandApi#getCommandHandler()
     */
    default boolean supportsCommandExecution() {
        return getCommandHandler().isPresent();
    }
}
