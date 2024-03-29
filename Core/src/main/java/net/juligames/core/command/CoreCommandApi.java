package net.juligames.core.command;

import net.juligames.core.Core;
import net.juligames.core.api.command.CommandApi;
import net.juligames.core.command.inbuild.InbuiltCommandManager;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Ture Bentzin
 * 05.12.2022
 */
public class CoreCommandApi implements CommandApi {

    public static final String COMMAND_NOTIFICATION_HEADER = "command";
    private static final CoreCommandNotificationListener listener = new CoreCommandNotificationListener();
    private final InbuiltCommandManager inbuiltManager = new InbuiltCommandManager();

    @Nullable
    private Consumer<String> commandHandler;

    public CoreCommandApi() {
        Core.getInstance().getNotificationApi().registerListener(listener);
    }

    /**
     * Execute a Command for the associated CommandHandling on the target instance. This is an unchecked operation!
     *
     * @param command the command to execute
     * @param targets the targets
     */
    @Override
    public void sendCommand(String command, UUID... targets) {
        Core.getInstance().getNotificationApi().getNotificationSender().sendNotification(COMMAND_NOTIFICATION_HEADER,
                command, targets);
    }

    /**
     * Executes a Command on every instance that matches the given Predicate
     *
     * @param command          the command to execute
     * @param targetIdentifier the predicate to identify the target
     */
    @Override
    public void sendCommand(String command, Predicate<UUID> targetIdentifier) {
        Core.getInstance().getClusterApi().getAllUUIDS().keySet().stream().filter(targetIdentifier)
                .forEach(uuid -> sendCommand(command, uuid));
    }

    /**
     * Not every Command will behave the same everywhere
     *
     * @param command the command to broadcast
     */
    @Override
    public void broadcastCommand(String command) {
        Core.getInstance().getNotificationApi().getNotificationSender().broadcastNotification(COMMAND_NOTIFICATION_HEADER,
                command);
    }

    /**
     * @return the consumer wich is called when a command should be executed on this instance
     * @apiNote if the optional is empty then the current instance does not support command execution
     * This should not be used for case 1 command handling use {@link CoreCommandApi#handle(String)} for this case
     */
    @Override
    public @MaybePresent Optional<Consumer<String>> getCommandHandler() {
        return Optional.ofNullable(commandHandler);
    }

    /**
     * Set the commandHandler
     *
     * @param commandHandler the new Handler. if commandHandler is null this will signal that this core does not accept command input
     * @return the old value
     * @apiNote This should be set as soon as the core is ready to receive commands
     */
    @SuppressWarnings("UnusedReturnValue")
    public Optional<Consumer<String>> setCommandHandler(@Nullable Consumer<String> commandHandler) {
        Optional<Consumer<String>> buffer = getCommandHandler();
        this.commandHandler = commandHandler;
        return buffer;
    }

    @ApiStatus.Experimental
    public InbuiltCommandManager getInbuiltManager() {
        return inbuiltManager;
    }

    /**
     * This will handle the command with the commandHandler. If the commandHandler is not present then nothing will happen
     *
     * @param command the command to handle
     * @apiNote if you try to handle "internal-debug-BIT-TEST" here nothing will be sent to the handler!
     */
    @ApiStatus.Internal
    protected void handle(@NotNull String command) {
        if (command.equalsIgnoreCase("internal-debug-BIT-TEST")) {
            Core.getInstance().getCoreLogger().info("BIT TEST!"); //DEBUG
            return;
        }
        getCommandHandler().ifPresent(stringConsumer -> {
            if (!inbuiltManager.handle(command)) stringConsumer.accept(command);
        });
    }

    protected final CoreCommandNotificationListener getListener() {
        return listener;
    }


}
