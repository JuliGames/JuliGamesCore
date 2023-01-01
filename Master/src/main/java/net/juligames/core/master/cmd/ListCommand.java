package net.juligames.core.master.cmd;

import com.hazelcast.cluster.Member;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.cluster.ClusterClient;
import net.juligames.core.api.cluster.ClusterMember;
import net.juligames.core.cluster.CoreClusterApi;

/**
 * @author Ture Bentzin
 * 30.12.2022
 */
public class ListCommand extends MasterCommand {

    public ListCommand() {
        super("List");
    }


    @Override
    public void executeCommand(String ignored) {
        CoreClusterApi clusterApi = Core.getInstance().getClusterApi();
        Logger coreLogger = Core.getInstance().getCoreLogger();
        coreLogger.info("listing Members:");
        for (ClusterMember member : clusterApi.getClusterMembers()) {
            coreLogger.info(member.getAttribute("name") + "[at]" + member.getSocketAddress());
        }
        coreLogger.info("listing Clients:");
        for (ClusterClient clusterClient : clusterApi.getClusterClients()) {
            coreLogger.info(clusterClient.getName() + "[at]" + clusterClient.getSocketAddress());
        }
    }
}
