package net.juligames.core.adventure.api.prompt.interpretation;

import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 18.05.2023
 */
public class PromptInterpreterUtils {

    private PromptInterpreterUtils() {
    }

    private abstract static class PromptInterpreter<T> implements Interpreter<T> {

        private @NotNull ValidatingPrompt<T> prompt;

        public PromptInterpreter(ValidatingPrompt<T>) {

        }
    }
}
