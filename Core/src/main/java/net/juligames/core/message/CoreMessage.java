package net.juligames.core.message;

import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MiniMessageSerializer;
import net.juligames.core.api.message.Replacement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessage implements Message {

    private final DBMessage messageData;
    private Set<CoreReplacement> replacements;

    public CoreMessage(@NotNull DBMessage messageData, Set<CoreReplacement> replacements) {
        this.messageData = messageData.clone(); //clone to avoid conflicts
        this.replacements = replacements;
    }

    public CoreMessage(@NotNull DBMessage messageData) {
        this.messageData = messageData.clone(); //clone to avoid conflicts
        this.replacements = Collections.unmodifiableSet(new HashSet<>());
    }

    @Contract("_,_,_ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData, String messageKey, Set<CoreReplacement> replacements) {
        if (messageData == null) {
            return new FallBackMessage(messageKey);
        } else {
            return new CoreMessage(messageData, replacements);
        }
    }

    @Deprecated
    @Contract("_,_ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData, Set<CoreReplacement> replacements) {
        if (messageData == null) {
            return new FallBackMessage();
        } else {
            return new CoreMessage(messageData, replacements);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData, String messageKey) {
        if (messageData == null) {
            return new FallBackMessage(messageKey);
        } else {
            return new CoreMessage(messageData);
        }
    }

    @Deprecated
    @Contract("_ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData) {
        if (messageData == null) {
            return new FallBackMessage();
        } else {
            return new CoreMessage(messageData);
        }
    }

    @Override
    public DBMessage getMessageData() {
        return messageData;
    }

    @Override
    public String getMiniMessage() {
        return messageData.getMiniMessage();
    }

    @Override
    public String getPreparedMiniMessage() {
        return null; //TODO
    }

    @Override
    public String getPlainMessage(@NotNull MiniMessageSerializer serializer) {
        return serializer.resolvePlain(this);
    }

    @Override
    @Deprecated
    public String getLegacyMessage(@NotNull MiniMessageSerializer serializer) {
        return serializer.resolveLegacy(this);
    }

    @Override
    public void doWithMiniMessage(@NotNull Function<String, String> actionToPerform) {
        String before = getMiniMessage();
        getMessageData().setMiniMessage(actionToPerform.apply(before));
    }

    @Override
    public Set<CoreReplacement> getReplacements() {
        return replacements;
    }

    @ApiStatus.Internal
    public void setReplacements(Collection<CoreReplacement> coreReplacements) {
        replacements = Set.copyOf(coreReplacements);
    }

    @Override
    public Message clone() {
        return new CoreMessage(messageData.clone(), replacements);
    }
}
