package net.juligames.core.command.inbuild;


import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.misc.TriConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ture Bentzin
 * 17.02.2023
 */
@ApiStatus.Experimental
@SuppressWarnings("unused")
public interface CommandExecutor {

    default @NotNull Logger getLogger() {
        return InbuiltCommandManager.staticLogger;
    }

    boolean execute(@NotNull InbuiltCommand command, @NotNull String input);

    default void executeAndForget(@NotNull InbuiltCommand command, @NotNull String input) {
        execute(command,input);
    }

    default void executeAndThen(@NotNull InbuiltCommand command, @NotNull String input, @NotNull TriConsumer<InbuiltCommand,String,Boolean> triConsumer) {
        triConsumer.consume(command,input, execute(command,input));
    }

    default void executeAndComplete(@NotNull InbuiltCommand command, @NotNull String input, @NotNull CompletableFuture<Boolean> completableFuture) {
       completableFuture.complete(execute(command,input));
    }
}
