package net.juligames.core.api.config;

import net.juligames.core.api.TODO;
import net.juligames.core.api.err.dev.TODOException;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * @author Ture Bentzin
 * 27.11.2022
 */
@TODO(doNotcall = true)
public class CollectionInterpreter<T> implements Interpreter<Collection<? extends T>>{

    private final Interpreter<T> tInterpreter;

    public CollectionInterpreter(Interpreter<T> tInterpreter) {
        this.tInterpreter = tInterpreter;
    }

    @Override
    public Collection<? extends T> interpret(final String input) throws Exception {
       /* String[] firstSplit = input.split("\\{");
        assert firstSplit.length > 1;
        int sizer = Integer.parseInt(firstSplit[0]);
        String data = input.replace(sizer + "","");
        */

        throw new TODOException();
    }


    @Override
    public String reverse(@NotNull Collection<? extends T> ts) {
        StringBuilder builder = new StringBuilder();

        StringJoiner innerJoiner = new StringJoiner(",");
        ts.forEach(t -> innerJoiner.add(appendObject(t)));
        builder.append(ts.size()).append(":");
        builder.append("{").append(innerJoiner).append("}");
        return builder.toString();
    }

    private @NotNull StringBuilder appendObject(T object){
        StringBuilder appender = new StringBuilder();
        appender.append("[").append(tInterpreter.reverse(object)).append("]");
        return appender;
    }

    public Interpreter<T> tInterpreter() {
        return tInterpreter;
    }
}
