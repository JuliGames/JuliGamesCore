package net.juligames.core.master.data;

import com.hazelcast.core.HazelcastInstance;
import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.jdbi.CoreSQLManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class MasterHazelInformationProvider {

    public static final String INFORMATION_MAP_NAME = "master_information";

    private final HazelcastInstance hazelcast;

    private @NotNull Map<String,String> informationMap() {
        return getHazelcast().getMap(INFORMATION_MAP_NAME);
    }

    public MasterHazelInformationProvider(HazelcastInstance hazelcast){
        this.hazelcast = hazelcast;
        genMap();
        update();
    }

    protected void genMap() {
        informationMap();
    }

    public void update() {
        //Locale
        DBLocale defaultEnglish = CoreSQLManager.defaultEnglish();
        informationMap().put("default_locale", defaultEnglish.getLocale());
        informationMap().put("default_locale_description", defaultEnglish.getDescription());
    }

    public HazelcastInstance getHazelcast() {
        return hazelcast;
    }
}
