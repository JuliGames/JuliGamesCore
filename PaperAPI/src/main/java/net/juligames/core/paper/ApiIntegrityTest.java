package net.juligames.core.paper;

import net.juligames.core.api.API;
import org.bukkit.Bukkit;

/**
 * @author Ture Bentzin
 * 21.11.2022
 */
public class ApiIntegrityTest {

    public static void main(String[] args) {
        //Test 1 sendMessage:
        PaperMessageRecipient paperMessageRecipient = new PaperMessageRecipient(Bukkit.getConsoleSender());
        API.get().getMessageApi().sendMessage("test.bommels", paperMessageRecipient, new String[]{"hallo", "hallo"});
    }
}
