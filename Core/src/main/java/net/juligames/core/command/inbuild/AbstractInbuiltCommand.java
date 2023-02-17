package net.juligames.core.command.inbuild;

import net.juligames.core.Core;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 17.02.2023
 */
@ApiStatus.Experimental
public abstract class AbstractInbuiltCommand extends InbuiltCommand {

    /**
     * @param name        the name
     * @param description the description
     * @param since       the version
     * @author Ture Bentzin
     * 17.02.2023
     */
    public AbstractInbuiltCommand(String name, String description, String since) {
        super(name, description, since, null);
        setExecutor(this);
    }

    /**
     * @param name        the name
     * @param description the description
     * @author Ture Bentzin
     * 17.02.2023
     */
    public AbstractInbuiltCommand(String name, String description) {
        super(name, description, Core.CORE_VERSION_NUMBER , null);
        setExecutor(this);
    }

    @Override
    public abstract boolean execute(InbuiltCommand command, String input);
}
