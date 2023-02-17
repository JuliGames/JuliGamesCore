package net.juligames.core.command.inbuild;


import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.misc.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ture Bentzin
 * 17.02.2023
 */
@SuppressWarnings("unused")
public interface CommandExecutor {

    default Logger getLogger() {
        return InbuiltCommandManager.staticLogger;
    }

    boolean execute(InbuiltCommand command, String input);

    default void executeAndForget(InbuiltCommand command, String input) {
        execute(command,input);
    }

    default void executeAndThen(InbuiltCommand command, String input, @NotNull TriConsumer<InbuiltCommand,String,Boolean> triConsumer) {
        triConsumer.consume(command,input, execute(command,input));
    }

    default void executeAndComplete(InbuiltCommand command, String input, @NotNull CompletableFuture<Boolean> completableFuture) {
       completableFuture.complete(execute(command,input));
    }
}
