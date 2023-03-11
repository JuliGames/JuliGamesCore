package net.juligames.core.notification;

import com.hazelcast.cluster.Address;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import de.bentzin.tools.register.Registerator;
import net.juligames.core.api.notification.NotificationApi;
import net.juligames.core.api.notification.NotificationListener;
import net.juligames.core.api.notification.NotificationSender;
import net.juligames.core.notification.debug.DebugNotificationPrinter;
import net.juligames.core.serialization.SerializedNotification;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class CoreNotificationApi implements NotificationApi, MessageListener<SerializedNotification> {

    private final Registerator<NotificationListener> listenerRegisterator;

    private final Set<Address> blacklist = new HashSet<>();

    public CoreNotificationApi() {
        listenerRegisterator = new Registerator<>();
    }

    @Override
    public boolean registerListener(@NotNull NotificationListener listener) {
        try {
            listenerRegisterator.register(listener);
            return true;
        } catch (Registerator.DuplicateEntryException ignore) {
            return false;
        }
    }

    @Override
    public boolean unregisterListener(@NotNull NotificationListener listener) {
        try {
            listenerRegisterator.unregister(listener);
            return true;
        } catch (Registerator.NoSuchEntryException ignored) {
            return false;
        }
    }

    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull NotificationSender getNotificationSender() {
        return new CoreNotificationSender();
    }

    public Collection<NotificationListener> getListeners() {
        return listenerRegisterator.getIndex().stream().toList();
    }

    /**
     * This is used for notification
     *
     * @return the current Blacklist
     */
    public Set<Address> getBlacklist() {
        return blacklist;
    }


    /**
     * handler for notificationsApi
     *
     * @param message the message that is received for the topic
     */
    @Override
    public void onMessage(Message<SerializedNotification> message) {
        //call Debug...
        new DebugNotificationPrinter().onMessage(message); //TODO This will stay until its finally tested under heavy load
        for (NotificationListener notificationListener : listenerRegisterator) {
            notificationListener.onNotification(message.getMessageObject().deserialize());
        }
    }
}
