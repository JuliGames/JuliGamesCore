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
 * This class represents a writer for Bukkit inventories that can be serialized into configurations.
 * It implements the ConfigWriter interface and provides methods for populating and writing an inventory to a configuration.
 * <p>
 * The class uses an Interpreter to convert ItemStacks to their serialized form, and vice versa.
 * <p>
 *
 * @author Ture Bentzin 23.01.2023
 */
public record InventoryConfigWriter(Inventory inventory) implements ConfigWriter {

    /**
     * The interpreter used to convert ItemStacks to their serialized form.
     */
    @SuppressWarnings("ProtectedMemberInFinalClass")
    protected static final @NotNull Interpreter<ItemStack> itemStackInterpreter = new ItemStackInterpreter();

    /**
     * Populates an inventory with items from the given configuration.
     *
     * @param configuration the configuration to read items from
     * @param keyspace      the keyspace in the configuration to read items from
     * @param inventory     the inventory to populate
     */
    public static void populateInventory(@NotNull Configuration configuration, @NotNull String keyspace, @NotNull Inventory inventory) {
        Collection<ItemStack> collection = configuration.getCollection(keyspace, itemStackInterpreter);
        inventory.setContents(collection.toArray(new ItemStack[0]));
    }

    /**
     * Populates an inventory with items from the given configuration and returns the inventory.
     * <p>
     *
     * @param configuration the configuration to read items from
     * @param keyspace      the keyspace in the configuration to read items from
     * @param inventory     the inventory to populate
     * @return the populated inventory
     */
    @ApiStatus.Experimental
    public static synchronized @NotNull Inventory populateAndReturnInventory(@NotNull Configuration configuration, @NotNull String keyspace,
                                                                             @NotNull Inventory inventory) {
        populateInventory(configuration, keyspace, inventory);
        return inventory;
    }

    /**
     * Writes the inventory to the given configuration.
     *
     * @param configuration the configuration to write the inventory to
     * @param keyspace      the keyspace in the configuration to write the inventory to
     */
    @Override
    public void write(@NotNull Configuration configuration, @NotNull String keyspace) {
        configuration.setIterable(keyspace, inventory, itemStackInterpreter);
    }
}
