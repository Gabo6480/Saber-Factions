package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class AbstractArgumentProvider<T> implements OptionalArgumentProvider{

    String name;
    String defaultValue;
    BiFunction<T, CommandContext, Boolean> filter;

    public AbstractArgumentProvider(@NotNull String name, String defaultValue, BiFunction<T, CommandContext, Boolean> filter){
        this.name = name;
        this.filter = filter;
        this.defaultValue = defaultValue;
    }


    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public List<String> getCompletions(CommandContext context, int currentArgumentIndex) {
        return getObjectCollection(context, currentArgumentIndex).stream()
                .filter(t -> filter.apply(t, context))
                .map(this::getObjectName).collect(Collectors.toList());
    }

    public abstract Collection<? extends T> getObjectCollection(CommandContext context, int currentArgumentIndex);
    public abstract String getObjectName(T object);

    @Override
    public @Nullable String getDefaultValue() {
        return defaultValue;
    }
}
