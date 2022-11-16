package net.juligames.core.data;

import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.hcast.HazelConnector;

import java.util.Map;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelDataCore implements HazelDataApi {
    /**
     * @param hazel the name of the map in the cluster
     * @return the map from hazelCast
     */
    @Override
    public <A, B> Map<A, B> getMap(String hazel) {
        return Core.
    }
}
