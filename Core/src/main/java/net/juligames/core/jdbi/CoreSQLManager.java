package net.juligames.core.jdbi;

import com.google.errorprone.annotations.DoNotCall;
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

    private final Jdbi jdbi;
    private final Logger logger;

    public CoreSQLManager(String connection, @NotNull Logger parentLogger) {
        logger = parentLogger.adopt("jdbi");
        /*
        Connection connection1 = null;
        try {
              connection1 = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            Core.getInstance().getCoreLogger().error("connection failed.. debug");
        }
        //jdbi = Jdbi.create(connection);
         */
        jdbi = Jdbi.create(connection);
        jdbi.installPlugin(new SqlObjectPlugin());
    }

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

    @Override
    @Hardcode //TODO: Make Dynamic
    @DoNotCall
    /**
     * This should only be called by a master instance. If you don't know when this method should be called or what a master
     * is then you are the one I have written this message for.
     */
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

        //replacementType
        logger.info("creating: replacementType");
        jdbi.withExtension(ReplacementTypeDAO.class, extension -> {
            extension.createTable();
            return null;
        });
        logger.info("created: replacementType");

        //replacement
        logger.info("creating: replacement");
        jdbi.withExtension(ReplacementDAO.class, extension -> {
            extension.createTable();
            return null;
        });
        logger.info("created: replacement");


    }

    protected Logger getLogger() {
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
