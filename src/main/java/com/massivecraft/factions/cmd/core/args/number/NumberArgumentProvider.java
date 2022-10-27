package com.massivecraft.factions.cmd.core.args.number;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.args.AbstractArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NumberArgumentProvider <N extends Number> extends AbstractArgumentProvider<N> {

    Function<String, N> parseFunction;

    public NumberArgumentProvider(@NotNull String name, N defaultValue, Function<String, N> parseFunction, BiFunction<N, CommandContext, Boolean> numberFilter) {
        super(name, defaultValue != null ? defaultValue.toString() : null, numberFilter);
        this.parseFunction = parseFunction;
    }

    public NumberArgumentProvider(String name, Function<String, N> parseFunction, BiFunction<N, CommandContext, Boolean> numberFilter){
        this(name, null, parseFunction, numberFilter);
    }

    public NumberArgumentProvider(String name, Function<String, N> parseFunction){
        this(name, null, parseFunction, (num, ctx) -> true);
    }

    @Override
    public Collection<? extends N> getObjectCollection(CommandContext context, int currentArgumentIndex) {

        List<N> completions = new ArrayList<>();

        String valueString = context.argAsString(currentArgumentIndex);

        N value;

        try {
            value = parseFunction.apply(valueString);

            for (int i = 0; i < 10; i++) {
                completions.add(parseFunction.apply(valueString + i));
            }

            if(value instanceof Double || value instanceof Float)
                for (int i = 0; i < 10; i++) {
                    completions.add(parseFunction.apply(valueString + "." + i));
                }
        }
        catch (Exception ignored){}

        return completions;
    }

    @Override
    public String getObjectName(N object) {
        return object.toString();
    }
}
