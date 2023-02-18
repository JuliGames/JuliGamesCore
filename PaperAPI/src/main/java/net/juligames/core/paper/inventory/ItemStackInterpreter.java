package net.juligames.core.paper.inventory;

import net.juligames.core.api.config.Interpreter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/*
byte[] b1 = new byte[] {-100, -128, -1, 1, -10, 100, 33, -13, 32, 87};
String bs = Base64.getEncoder().encodeToString(b1);
byte[] b2 = Base64.getDecoder().decode(bs);
System.out.println(Arrays.equals(b1, b2));

https://www.toptip.ca/2019/04/java-convert-byte-array-to-string-then.html
 */

/**
 * @author Ture Bentzin
 * 23.01.2023
 * @apiNote NOTE: This format is <bold>not</bold> human-readable!!! Not even a bit!!! It's saved as fucking bytes and encodes them in fucking base64!!
 */
public class ItemStackInterpreter implements Interpreter<ItemStack> {


    @Override
    public @NotNull ItemStack interpret(String input) throws IllegalArgumentException {
        return ItemStack.deserializeBytes(fromBase64String(input));
    }

    @Override
    public @NotNull String reverse(@NotNull ItemStack itemStack) {
        return toBase64String(itemStack.serializeAsBytes());
    }

    protected byte[] fromBase64String(@NotNull String base64) throws IllegalArgumentException {
        //noinspection ConstantConditions
        if (base64 != null && !base64.isEmpty() && !base64.isBlank())
            return Base64.getDecoder().decode(base64);
        else
            return new byte[0];
    }

    @NotNull
    protected String toBase64String(byte @NotNull [] bytes) {
        //noinspection ConstantConditions
        if (bytes != null && bytes.length != 0)
            return Base64.getEncoder().encodeToString(bytes);
        else
            return "";
    }
}
