package net.juligames.core.notification;

import de.bentzin.tools.register.Registerator;
import net.juligames.core.api.notification.NotificationApi;
import net.juligames.core.api.notification.NotificationListener;
import net.juligames.core.api.notification.NotificationSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class CoreNotificationApi implements NotificationApi {

    private Registerator<NotificationListener> listenerRegisterator;

    @Override
    public boolean registerListener(NotificationListener listener) {
        try {
            listenerRegisterator.register(listener);
            return true;
        } catch (Registerator.DuplicateEntryException ignore) {
            return false;
        }
    }

    @Override
    public boolean unregisterListener(NotificationListener listener) {
        try {
            listenerRegisterator.unregister(listener);
            return true;
        }catch (Registerator.NoSuchEntryException ignored){
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
}
