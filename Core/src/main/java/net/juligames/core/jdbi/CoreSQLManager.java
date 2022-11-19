package net.juligames.core.jdbi;

import de.bentzin.tools.Hardcode;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.*;
import net.juligames.core.api.jdbi.mapper.bean.LocaleBean;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreSQLManager implements SQLManager {

    /**
     * This does not save anything to the DB it just gets if there is a locale for EN_US and returns it when found and if not it will generate one!
     */
    @Hardcode
    public static DBLocale defaultEnglish() {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(LocaleDAO.class, extension -> {
            List<DBLocale> locales = extension.listAll();
            for (DBLocale locale : locales) {
                if (locale.getLocale().equals("EN_US")) {
                    return locale;
                }
            }
            return new LocaleBean("EN_US", "English (USA)");
        });
    }


    private final Jdbi jdbi;
    private final Logger logger;


    public CoreSQLManager(String connection, @NotNull Logger parentLogger) {
        logger = parentLogger.adopt("jdbi");
        jdbi = Jdbi.create(connection);
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @Override
    @Hardcode //TODO: Make Dynamic
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


        //player_locale_preference
        logger.info("creating: player_locale_preference");
        jdbi.withExtension(PlayerLocalPreferenceDAO.class, extension -> {
            extension.createTable();
            return null;
        });
        logger.info("created: player_locale_preference");
    }

    protected Logger getLogger(){
        return logger;
    }

    @Override
    public Jdbi getJdbi() {
        return jdbi;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("JDBI is currently: ").append(jdbi).toString();
    }
}
