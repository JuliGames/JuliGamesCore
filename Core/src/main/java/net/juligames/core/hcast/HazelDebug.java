package net.juligames.core.hcast;

import net.juligames.core.Core;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.MessageDAO;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;

import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelDebug {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Core core = new Core();
        core.start("test");
        core.await();
        System.out.println("core should be online! - testing db");
        API.get().getSQLManager().getJdbi().withExtension(MessageDAO.class, extension -> {
            extension.insert(new MessageBean("test.test", "EN_US", "<gray>hallo"));
            return null;
        });
    }
}
