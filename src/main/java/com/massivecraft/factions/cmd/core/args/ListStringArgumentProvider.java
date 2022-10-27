package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ListStringArgumentProvider extends AbstractArgumentProvider<String>{

    List<String> values;

    public ListStringArgumentProvider(String name, String defaultValue, String... values){
        this(name, defaultValue, Arrays.stream(values).collect(Collectors.toList()));
    }

    public ListStringArgumentProvider(String name, List<String> values){
        this(name, null, values);
    }

    public ListStringArgumentProvider(String name, String defaultValue, List<String> values){
        super(name, defaultValue, ((s, context) -> true));
        this.values = new ArrayList<>(values);
    }

    @Override
    public Collection<? extends String> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return values;
    }

    @Override
    public String getObjectName(String object) {
        return object;
    }
}
