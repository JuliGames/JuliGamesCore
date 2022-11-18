package net.juligames.core.master.sql;

import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.api.jdbi.LocaleDAO;
import net.juligames.core.api.jdbi.MessageDAO;
import net.juligames.core.api.jdbi.SQLManager;
import net.juligames.core.api.jdbi.mapper.bean.LocaleBean;
import net.juligames.core.master.CoreMaster;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class MasterSQLManager implements SQLManager {

    /**
     * This does not save anything to the DB it just gets if there is a locale for EN_US and returns it when found and if not it will generate one!
     *
     * @return
     */
    public static DBLocale defaultEnglish() {
        return CoreMaster.getMasterSQLManager().getJdbi().withExtension(LocaleDAO.class, extension -> {
            List<DBLocale> locales = extension.listAll();
            for (DBLocale locale : locales) {
                if (locale.getLocale().equals("EN_US")) {
                    return locale;
                }
            }
            return new LocaleBean("EN_US", "English (USA)");
        });
    }

    public static final Logger logger = LoggerFactory.getLogger(MasterSQLManager.class);

    private final Jdbi jdbi;

    public MasterSQLManager(String connection) {
        jdbi = Jdbi.create(connection);
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @Override
    public void createTables() {
        //Locale
        logger.info("creating: locale");
        jdbi.withExtension(LocaleDAO.class, extension -> {
            extension.createTable();
            return null;
        });
        logger.info("created: locale");

        //Message
        logger.info("creating: message");
        jdbi.withExtension(MessageDAO.class, extension -> {
            extension.createTable();
            return null;
        });
        logger.info("created: message");
    }


    @Override
    public Jdbi getJdbi() {
        return jdbi;
    }
}
