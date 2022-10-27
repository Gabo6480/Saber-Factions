package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public class CompositeArgumentProvider implements OptionalArgumentProvider {
    String name;
    String defaultValue;

    ArgumentProvider[] subProviders;

    public CompositeArgumentProvider(String name, String defaultValue, ArgumentProvider... subProviders){

        if(name != null)
            this.name = name;
        else {
            StringBuilder nameBuilder = new StringBuilder();
            for (ArgumentProvider provider : subProviders)
                nameBuilder.append(provider.getName()).append("|");
            this.name = nameBuilder.toString();
        }

        this.defaultValue = defaultValue;
        this.subProviders = subProviders;
    }

    public CompositeArgumentProvider(String name, ArgumentProvider... subProviders){
        this(name, null, subProviders);
    }

    public CompositeArgumentProvider(ArgumentProvider... subProviders){
        this(null, null, subProviders);
    }

    @Override
    public List<String> getCompletions(CommandContext context, int currentArgumentIndex) {
        List<String> completions = new ArrayList<>();

        for (ArgumentProvider provider : subProviders){
            List<String> subCompletions = provider.getCompletions(context, currentArgumentIndex);
            if(subCompletions != null)
                completions.addAll(subCompletions);
        }


        return completions;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}
