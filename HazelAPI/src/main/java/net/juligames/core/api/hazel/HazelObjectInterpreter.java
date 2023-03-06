package net.juligames.core.api.hazel;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 06.03.2023
 */
public class HazelObjectInterpreter implements Interpreter<DistributedObject> {

    private final @NotNull Supplier<Collection<DistributedObject>> collectionSupplier;

    @Contract(pure = true)
    public HazelObjectInterpreter(@NotNull HazelcastInstance instance) {
        collectionSupplier = instance::getDistributedObjects;
    }

    public HazelObjectInterpreter(@NotNull Supplier<Collection<DistributedObject>> collectionSupplier) {
        this.collectionSupplier = collectionSupplier;
    }

    @Contract("_ -> new")
    public static @NotNull HazelObjectInterpreter get(@NotNull NativeHazelDataAPI nativeHazelDataAPI) {
        return new HazelObjectInterpreter(nativeHazelDataAPI::getAll);
    }

    @Override
    public @NotNull DistributedObject interpret(String input) throws Exception {
        return collectionSupplier.get().stream().filter(distributedObject ->
                distributedObject.getName().equals(input)).findFirst().orElseThrow();
    }

    @Override
    public @NotNull String reverse(@NotNull DistributedObject distributedObject) {
        return distributedObject.getName();
    }
}
