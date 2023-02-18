package net.juligames.core.paper.inventory;

import net.juligames.core.api.config.ConfigWriter;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.Interpreter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Ture Bentzin
 * 23.01.2023
 */
public record InventoryConfigWriter(Inventory inventory) implements ConfigWriter {

    @SuppressWarnings("ProtectedMemberInFinalClass")
    protected static final @NotNull Interpreter<ItemStack> itemStackInterpreter = new ItemStackInterpreter();

    public static void populateInventory(@NotNull Configuration configuration, @NotNull String keyspace, @NotNull Inventory inventory) {
        Collection<ItemStack> collection = configuration.getCollection(keyspace, itemStackInterpreter);
        inventory.setContents(collection.toArray(new ItemStack[0]));
    }

    @ApiStatus.Experimental
    public static synchronized @NotNull Inventory populateAndReturnInventory(@NotNull Configuration configuration, @NotNull String keyspace,
                                                                             @NotNull Inventory inventory) {
        populateInventory(configuration, keyspace, inventory);
        return inventory;
    }

    @Override
    public void write(@NotNull Configuration configuration, @NotNull String keyspace) {
        configuration.setIterable(keyspace, inventory, itemStackInterpreter);
    }
}
