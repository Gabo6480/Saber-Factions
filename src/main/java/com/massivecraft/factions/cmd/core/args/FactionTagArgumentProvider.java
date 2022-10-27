package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.CommandContext;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FactionTagArgumentProvider extends AbstractArgumentProvider<Faction>{

    public FactionTagArgumentProvider(){
        super("faction",null, (f, ctx) -> true);
    }

    public FactionTagArgumentProvider(@NotNull String name){
        super(name,null, (f, ctx) -> true);
    }

    public FactionTagArgumentProvider(BiFunction<Faction, CommandContext, Boolean> factionFilter){
        super("faction",null, factionFilter);
    }

    public FactionTagArgumentProvider(@NotNull String name, String defaultValue, BiFunction<Faction, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
    }

    public FactionTagArgumentProvider(@NotNull String name, BiFunction<Faction, CommandContext, Boolean> filter) {
        super(name, null, filter);
    }

    @Override
    public Collection<? extends Faction> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return Factions.getInstance().getAllFactions();
    }

    @Override
    public String getObjectName(Faction object) {
        return ChatColor.stripColor(object.getTag());
    }
}
