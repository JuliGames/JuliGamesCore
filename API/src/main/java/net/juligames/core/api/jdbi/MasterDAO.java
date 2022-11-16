package net.juligames.core.api.jdbi;

import net.juligames.core.api.jdbi.Locale;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;

import java.util.List;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface MasterDAO {

    void createTable();

    List<Locale> listAll();

    void insertBean(@BindBean Locale locale);

    void deleteBean(@Bind("locale") String locale);

    DeseBean selectBean(@Bind("locale") String locale);

}
