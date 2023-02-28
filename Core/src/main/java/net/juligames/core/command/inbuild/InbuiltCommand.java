package net.juligames.core.command.inbuild;

import net.juligames.core.Core;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * 17.02.2023
 */
@ApiStatus.Experimental
public class InbuiltCommand implements CommandExecutor {
    private final String name;
    private final String description;
    private final String since;
    private CommandExecutor executor;

    public InbuiltCommand(String name, String description, String since, @Nullable CommandExecutor executor) {
        this.name = name;
        this.description = description;
        this.since = since;
        this.executor = executor;

    }

    @Override
    public boolean execute(@NotNull InbuiltCommand command, @NotNull String input) {
        if (executor() != this) {
            return executor().execute(command, input);
        } else if (executor != null) {
            throw new IllegalArgumentException(executor.getClass().getName() + " not allowed here!");
        }
        throw new NoSuchElementException("no executor!");
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }

    public final String since() {
        return since;
    }

    public final CommandExecutor executor() {
        return executor;
    }

    protected final void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    protected final boolean acceptsWithCurrent(String input) {
        return accepts(input, Core.CORE_VERSION_NUMBER);
    }

    /**
     * Currently the version is not used!
     * This will be changed, if significant changes are made with this commandSystem
     */
    @SuppressWarnings("SameParameterValue")
    protected boolean accepts(@NotNull String input, String ignored) {
        return input.toLowerCase().startsWith(name.toLowerCase() + " ");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        InbuiltCommand that = (InbuiltCommand) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.since, that.since) &&
                Objects.equals(this.executor, that.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, since, executor);
    }

    @Override
    public String toString() {
        return "InbuiltCommand: " + name() + " : " + description();
    }

}
