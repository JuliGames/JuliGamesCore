package net.juligames.core.master.cmd;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.map.IMap;
import net.juligames.core.Core;
import net.juligames.core.api.TODO;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Ture Bentzin
 * 01.01.2023
 */
@TODO(doNotcall = false)
public class PrintMapCommand extends MasterCommand {

    public PrintMapCommand() {
        super("printMap");
    }

    @Override
    public void executeCommand(@NotNull String commandString) {
        String[] s = commandString.replaceFirst(" ", "").split(" ");
        String ident = s[0];
        Optional<DistributedObject> optionalDistributedObject =
                Core.getInstance().getOrThrow().getDistributedObjects().stream()
                        .filter(distributedObject -> distributedObject.getName().equals(ident))
                        .findFirst();
        if (optionalDistributedObject.isEmpty()) {
            throw new IllegalArgumentException("cant find: " + ident);
        }
        IMap<Object, Object> map = Core.getInstance().getOrThrow().getMap(ident);
        Core.getInstance().getCoreLogger().info("printing result of printMap: " + ident);
        final boolean[] first = {true};
        map.forEach((o, o2) -> {

            if (first[0]) {
                first[0] = false;
                Core.getInstance().getCoreLogger().warning("The here referenced classes may be inaccurate");
                Core.getInstance().getCoreLogger().info("keyClass: " + o.getClass().getSimpleName());
                Core.getInstance().getCoreLogger().info("valueClass: " + o2.getClass().getSimpleName());
            }

            Core.getInstance().getCoreLogger().info(o.toString() + " : " + o2.toString());

        });
    }
}
