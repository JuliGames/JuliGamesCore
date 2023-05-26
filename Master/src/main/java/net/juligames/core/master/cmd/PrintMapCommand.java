package net.juligames.core.master.cmd;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.map.IMap;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.TODO;
import net.juligames.core.api.misc.APIUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static net.juligames.core.api.misc.APIUtils.executeAndReturnFirstSuccess;
import static net.juligames.core.api.misc.APIUtils.executeAndSwallow;

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
        final IMap<Object, Object> map = findMap(ident);
        Logger coreLogger = Core.getInstance().getCoreLogger();
        coreLogger.info("printing result of printMap: " + map.getName());

        AtomicReference<Class<?>> keyClass = new AtomicReference<>();
        AtomicReference<Class<?>> valueClass = new AtomicReference<>();
        map.forEach((o, o2) -> {

            if (o == null || o2 == null) {
                coreLogger.warning("FLAWED: " + o + " :! " + o2);
                return;
            }

            if (keyClass.get() != o.getClass() || valueClass.get() != o2.getClass()) {
                coreLogger.info(o.getClass().getSimpleName() + " | " + o2.getClass().getSimpleName());
                keyClass.set(o.getClass());
                valueClass.set(o2.getClass());
            }

            coreLogger.info(o + " : " + o2);

        });
    }


    @SuppressWarnings("unchecked")
    protected <K, V> IMap<K, V> findMap(String query) {
        return (IMap<K, V>) executeAndReturnFirstSuccess(
                //Equal names
                () -> findMapHard(query,distributedObject -> distributedObject.getName().equals(query)),
                //Semi Equal names
                () -> findMapHard(query,distributedObject -> distributedObject.getName().equalsIgnoreCase(query)),
                //Starts with
                () -> findMapHard(query, distributedObject -> distributedObject.getName().startsWith(query)),
                //Starts with (semi)
                () -> findMapHard(query, distributedObject -> distributedObject.getName().toLowerCase().startsWith(query.toLowerCase())),
                //Contains
                () -> findMapHard(query, distributedObject -> distributedObject.getName().contains(query)),
                //Contains (semi)
                () -> findMapHard(query, distributedObject -> distributedObject.getName().toLowerCase().contains(query.toLowerCase()))

        ).orElseThrow(() -> new IllegalArgumentException("cant find a map for query: \"" + query + "\""));
    }

    @SuppressWarnings("unchecked")
    private <K, V> @NotNull IMap<K, V> findMapHard(String name, Predicate<DistributedObject> predicate) {
        Optional<DistributedObject> optionalDistributedObject =
                Core.getInstance().getOrThrow().getDistributedObjects().stream()
                        .filter(predicate)
                        .findFirst();
        if (optionalDistributedObject.isEmpty()) {
            throw new IllegalArgumentException("cant find: " + name);
        }
        return (IMap<K, V>)optionalDistributedObject.get();
    }
}
