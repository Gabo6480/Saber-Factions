package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SingleWordArgumentProvider implements OptionalArgumentProvider{
    String name;
    String defaultValue;

    List<String> values;
    BiFunction<String, CommandContext, Boolean> wordRejector;

    public SingleWordArgumentProvider(String name, String defaultValue, BiFunction<String, CommandContext, Boolean> wordRejector){
        this.name = name;
        this.defaultValue = defaultValue;
        this.wordRejector = wordRejector;

        values = new ArrayList<>();
        values.add("[<" + name + ">]");
    }

    public SingleWordArgumentProvider(String name, BiFunction<String, CommandContext, Boolean> wordRejector){
        this(name, null, wordRejector);
    }

    public SingleWordArgumentProvider(String name){
        this(name, null, (w, c) -> true);
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getCompletions(CommandContext context, int currentArgumentIndex) {
        String word = context.argAsString(currentArgumentIndex);


        return wordRejector.apply(word, context) ? values : new ArrayList<>();
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}
