package net.juligames.core.jdbi;

import com.google.errorprone.annotations.DoNotCall;
import de.bentzin.tools.Hardcode;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.*;
import net.juligames.core.api.jdbi.mapper.bean.LocaleBean;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
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

    /**
     * This should only be called by a master instance. If you don't know when this method should be called or what a master
     * is then you are the one I have written this message for.
     */
    @Override
    @Hardcode //TODO: Make Dynamic
    @DoNotCall
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


        //Currently not automatically used
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

        //EXPERIMENTAL: data
        logger.info("creating: data");
        jdbi.withExtension(DataDAO.class, extension -> {
            extension.createTable();
            return null;
        });
        logger.info("created: data");


    }

    protected Logger getLogger() {
        return logger;
    }

    @Override
    public Jdbi getJdbi() {
        return jdbi;
    }

    @Override
    @ApiStatus.Experimental
    public String getData(String key) {
        return getJdbi().withExtension(DataDAO.class,extension -> extension.selectBean(key)).getData();
    }

    @Override
    public Handle openHandle() {
        return getJdbi().open();
    }

    @Override
    public Jdbi getCustomJDBI(Connection connection) {
        return Jdbi.create(connection);
    }

    @Override
    public ConfigRegistry getConfig() {
        return getJdbi().getConfig().createCopy();
    }

    @Override
    public String toString() {
        return "JDBI is currently: " + jdbi;
    }
}
