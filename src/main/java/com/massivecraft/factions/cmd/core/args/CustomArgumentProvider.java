package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.struct.Warp;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomArgumentProvider<T> extends AbstractArgumentProvider<T>{

    BiFunction<CommandContext, Integer, Collection<? extends T>> objectCollectionGetter;
    Function<T, String> objectNameGetter;

    public CustomArgumentProvider(@NotNull String name, String defaultValue, BiFunction<CommandContext, Integer, Collection<? extends T>> objectCollectionGetter, Function<T, String> objectNameGetter, BiFunction<T, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
        this.objectCollectionGetter = objectCollectionGetter;
        this.objectNameGetter = objectNameGetter;
    }
    public CustomArgumentProvider(@NotNull String name, String defaultValue, BiFunction<CommandContext, Integer, Collection<? extends T>> objectCollectionGetter, Function<T, String> objectNameGetter) {
        this(name, defaultValue, objectCollectionGetter, objectNameGetter, (t, ctx) -> true);
    }

    @Override
    public Collection<? extends T> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return objectCollectionGetter.apply(context, currentArgumentIndex);
    }

    @Override
    public String getObjectName(T object) {
        return objectNameGetter.apply(object);
    }
}
