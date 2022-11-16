package net.juligames.core.hcast;

import com.hazelcast.map.IMap;

import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelDebug {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        HazelConnector hazelConnector = HazelConnector.getInstanceAndConnect("test");
        IMap<String,String> bommelsMap = hazelConnector.getInstance().get().getMap("BommelsMap");
        bommelsMap.put("micheal","MICHEAL");

        IMap<String,String> bommelsMap1 = hazelConnector.getInstance().get().getMap("BommelsMap1");
        bommelsMap1.put("micheal","MICHEAL");

        IMap<String,String> bommelsMap2 = hazelConnector.getInstance().get().getMap("BommelsMap2");
        bommelsMap2.put("micheal","MICHEAL");

        IMap<String,String> bommelsMap3 = hazelConnector.getInstance().get().getMap("BommelsMap3");
        bommelsMap3.put("micheal","MICHEAL");
    }
}
