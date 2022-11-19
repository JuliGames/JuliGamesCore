package net.juligames.core.message.adventure;

import com.google.errorprone.annotations.DoNotCall;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.DBReplacement;
import net.juligames.core.api.jdbi.ReplacementTypeDAO;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementTypeBean;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.intellij.lang.annotations.Subst;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class JDBITagAdapter {

    public static Logger logger = Core.getInstance().getCoreLogger().adopt("adapter");
    public static Tag fromJDBI(@NotNull DBReplacement replacement) {
        String s = replacement.replacementType();
        ReplacementType replacementType = ReplacementType.valueOf(s);
        switch (replacementType) {

            case TEXT_CLOSING: {
                return Tag.selfClosingInserting(resolveValue(replacement));
            }
            case TEXT: {
                return Tag.inserting(resolveValue(replacement));
            }
            case COLOR_HEX:{
                return Tag.styling(style -> style.color(TextColor.fromHexString(replacement.value())));
            }
            case COLOR_HEX_CSS:{
                return Tag.styling(style -> style.color(TextColor.fromCSSHexString(replacement.value())));
            }
            case NAMED_COLOR:{
                return Tag.styling(style -> style.color(NamedTextColor.NAMES.value(replacement.value())));
            }
            case FONT:{
                return Tag.styling(style -> style.font(Key.key(replacement.value())));
            }
            case INSERT:{
                return Tag.styling(style -> style.insertion(replacement.value()));
            }
            case PROCESS:{
                return Tag.preProcessParsed(replacement.value());
            }


            default:{
                logger.warning("unknown replacement : " + s);
                return Tag.preProcessParsed(s);
            }
        }
    }

    /**
     *WARNING: This uses the fallback resolver
     */
    private static @NotNull Component resolveValue(@NotNull DBReplacement replacement) {
        return Core.getInstance().getMessageApi().getTagManager().fallbackResolve(replacement.value());
    }

    public enum ReplacementType {
        TEXT_CLOSING,
        TEXT,
        COLOR_HEX,
        COLOR_HEX_CSS,
        NAMED_COLOR,
        FONT,
        INSERT,
        PROCESS;

        /**
         * @see net.juligames.core.jdbi.CoreSQLManager
         */
        @DoNotCall
        @ApiStatus.Internal
        public static void defaultToJDBI(@NotNull Jdbi jdbi){
            jdbi.withExtension(ReplacementTypeDAO.class,extension -> {
                for (ReplacementType value : values()) {
                    extension.insert(new ReplacementTypeBean(value.name()));
                }
                return null;
            });
        }
    }
}
