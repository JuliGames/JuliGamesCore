package net.juligames.core.velocity.perms;

import com.velocitypowered.api.permission.PermissionSubject;
import de.bentzin.tools.pair.DividedPair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Ture Bentzin
 * 29.01.2023
 */
public final class PermissionConditions {

    private PermissionConditions() {

    }

    @Contract("_, _ -> new")
    public static @NotNull PermissionCheckReturn hasPermission(PermissionSubject permissible, String permission) {
        return PermissionConditionReturn.check(permissible, permission);
    }

    @Contract("_, _ -> new")
    public static @NotNull DividedPair<Boolean, Collection<PermissionCheckReturn>> hasPermissions(PermissionSubject permissible,
                                                                                                  @NotNull Collection<String> permissions) {

        ArrayList<PermissionCheckReturn> permissionCheckReturns = new ArrayList<>();
        boolean failed = false;
        for (String s : permissions) {
            PermissionCheckReturn check = PermissionConditionReturn.check(permissible, s);
            permissionCheckReturns.add(check);
            if (!check.getResult()) failed = true;
        }
        return new DividedPair<>(!failed, permissionCheckReturns);
    }


    static final class PermissionConditionReturn implements PermissionCheckReturn {

        private final PermissionSubject permissible;
        private final boolean success;
        private final boolean result;
        private final String permission;
        private final List<Exception> exceptions;

        public PermissionConditionReturn(PermissionSubject permissible, boolean success, boolean result, String permission, List<Exception> exceptions) {
            this.permissible = permissible;
            this.success = success;
            this.result = result;
            this.permission = permission;
            this.exceptions = exceptions;
        }

        public PermissionConditionReturn(PermissionSubject permissible, boolean result, String permission, @Nullable List<Exception> exceptions) {
            this.permissible = permissible;

            if (exceptions == null)
                success = true;
            else
                success = exceptions.isEmpty();

            this.result = result;
            this.permission = permission;
            this.exceptions = exceptions;
        }

        @Contract("_, _ -> new")
        static @NotNull PermissionCheckReturn check(PermissionSubject permissible, String permission) {
            boolean b = false;
            final List<Exception> exceptions1 = new ArrayList<>();
            try {
                b = permissible.hasPermission(permission);
            } catch (RuntimeException e) {
                exceptions1.add(e);
            }
            return new PermissionConditionReturn(permissible, b, permission, exceptions1);
        }

        @Override
        public PermissionSubject permissible() {
            return permissible;
        }

        @Override
        public String permissionName() {
            return permission;
        }

        @Override
        public boolean wasSuccessful() {
            return success;
        }

        @Override
        public boolean getResult() {
            return success && result;
        }

        @Override
        public boolean checkAndContinue() {
            sendLackingPermissionMessageIfPossible();
            return getResult();
        }

        @Override
        public boolean checkAndContinue(@NotNull BiConsumer<PermissionSubject, Boolean> resultConsumer) {
            sendLackingPermissionMessageIfPossible();
            resultConsumer.accept(permissible, result);
            return getResult();
        }

        @Override
        public List<Exception> exceptions() {
            return exceptions;
        }
    }
}
