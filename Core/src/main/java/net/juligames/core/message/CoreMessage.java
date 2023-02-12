package net.juligames.core.message;

import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MiniMessageSerializer;
import net.juligames.core.api.message.PatternType;
import org.jetbrains.annotations.*;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessage implements Message {

    private final DBMessage messageData;
    private Map<Integer, String> replacements;

    public CoreMessage(@NotNull DBMessage messageData, Map<Integer, String> replacements) {
        this.messageData = messageData.clone(); //clone to avoid conflicts
        this.replacements = Map.copyOf(replacements);
    }

    public CoreMessage(@NotNull DBMessage messageData) {
        this.messageData = messageData.clone(); //clone to avoid conflicts
        this.replacements = Map.of();
    }

    @Contract("_,_,_ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData, String messageKey, Map<Integer, String> replacements) {
        if (messageData == null) {
            return new FallBackMessage(messageKey);
        } else {
            return new CoreMessage(messageData, replacements);
        }
    }

    @Deprecated
    @Contract("_,_ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData, Map<Integer, String> replacements) {
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
        String mini = getMiniMessage();
        for (Map.Entry<Integer, String> entry : getReplacementSet())
            for (PatternType value : PatternType.values())
                mini = mini.replace(value.buildPattern(entry.getKey()), entry.getValue());
        return mini;
    }

    @Override
    public String getMiniMessageReadyForResolving(@Range(from = 0, to = Integer.MAX_VALUE) int replacementSize) {
        String mini = getMiniMessage();
        for (int i = 0; i < replacementSize; i++)
            for (PatternType patternType : PatternType.values())
                mini = patternType.convertPatternToTag(mini, i);
        return mini;
    }

    @Override
    public String getMiniMessageReadyForResolving() {
        return getMiniMessageReadyForResolving(replacementSize());
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
    public Map<Integer, String> getReplacements() {
        return replacements;
    }

    @ApiStatus.Internal
    public void setReplacements(Map<Integer, String> replacements) {
        this.replacements = Map.copyOf(replacements);
    }

    @Override
    public Set<Map.Entry<Integer, String>> getReplacementSet() {
        return Set.copyOf(replacements.entrySet());
    }

    @Override
    public Message clone() {
        return new CoreMessage(messageData.clone(), replacements);
    }
}
