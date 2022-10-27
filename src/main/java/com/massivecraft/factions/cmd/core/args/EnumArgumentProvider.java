package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;

public class EnumArgumentProvider<E extends Enum<E>> extends AbstractArgumentProvider<E>{

    Class<E> enumType;

    public EnumArgumentProvider(Class<E> enumType, @NotNull String name, String defaultValue, BiFunction<E, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
        this.enumType = enumType;
    }
    public EnumArgumentProvider(Class<E> enumType, @NotNull String name, String defaultValue) {
        this(enumType, name, defaultValue, (e, ctx) -> true);
    }

    @Override
    public Collection<? extends E> getObjectCollection(CommandContext context, int currentArgumentIndex) {

        return Arrays.asList(enumType.getEnumConstants());
    }

    @Override
    public String getObjectName(E object) {
        return object.name().toLowerCase();
    }
}
