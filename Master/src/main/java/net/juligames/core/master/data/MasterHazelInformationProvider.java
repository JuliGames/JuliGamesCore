package net.juligames.core.master.data;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nonapi.io.github.classgraph.utils.VersionFinder;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.jdbi.CoreSQLManager;
import net.juligames.core.master.CoreMaster;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.lang.module.Configuration;
import java.util.Map;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class MasterHazelInformationProvider {

    public static final String INFORMATION_MAP_NAME = "master_information";

    private final HazelcastInstance hazelcast;

    public MasterHazelInformationProvider(HazelcastInstance hazelcast) {
        this.hazelcast = hazelcast;
        genMap();
        update();
    }

    private @NotNull Map<String, String> informationMap() {
        return getHazelcast().getMap(INFORMATION_MAP_NAME);
    }

    protected void genMap() {
        informationMap();
    }

    public void update() {
        //Locale
        DBLocale defaultEnglish = CoreSQLManager.defaultEnglish();
        informationMap().put("default_locale", defaultEnglish.getLocale());
        informationMap().put("default_locale_description", defaultEnglish.getDescription());
        informationMap().put("master_uuid", hazelcast.getLocalEndpoint().getUuid().toString());
        informationMap().put("master_name", hazelcast.getName());
        informationMap().put("master_boot", String.valueOf(CoreMaster.getBootMillis().orElseThrow()));
        informationMap().put("master_version", API.get().getVersion());
        informationMap().put("last_update", System.currentTimeMillis() + "");
        //Experimental
        informationMap().put("master_commands", CoreMaster.getMasterCommandRunner().getIndex() + "");
        informationMap().put("hazelcast_version", VersionFinder.getVersion());
        informationMap().put("master_os", VersionFinder.OS.name());
        informationMap().put("java_version", VersionFinder.JAVA_VERSION);
    }

    public HazelcastInstance getHazelcast() {
        return hazelcast;
    }
}
