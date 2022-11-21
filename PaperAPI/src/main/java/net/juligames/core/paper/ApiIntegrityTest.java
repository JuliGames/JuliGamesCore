package net.juligames.core.paper;

import io.papermc.paper.entity.Bucketable;
import net.juligames.core.api.API;
import net.juligames.core.api.message.MessageRecipient;
import org.bukkit.Bukkit;

import java.util.Collection;

/**
 * @author Ture Bentzin
 * 21.11.2022
 */
public class ApiIntegrityTest {

    public static void main(String[] args) {
        //Test 1 sendMessage:
        PaperMessageRecipient paperMessageRecipient = new PaperMessageRecipient(Bukkit.getConsoleSender());
        API.get().getMessageApi().sendMessage("test.bommels",paperMessageRecipient);
    }
}
