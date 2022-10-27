package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.CommandContext;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FPlayerAndFactionArgumentProvider extends CompositeArgumentProvider{
    public FPlayerAndFactionArgumentProvider(){
        this("target",null, (fp, ctx) -> true, (f, ctx) -> true);
    }

    public FPlayerAndFactionArgumentProvider(@NotNull String name){
        this(name,null,(fp, ctx) -> true, (f, ctx) -> true);
    }

    public FPlayerAndFactionArgumentProvider(BiFunction<FPlayer, CommandContext, Boolean> fPlayerFilter, BiFunction<Faction, CommandContext, Boolean> factionFilter){
        this("target",null,fPlayerFilter, factionFilter);
    }

    public FPlayerAndFactionArgumentProvider(@NotNull String name, String defaultValue, BiFunction<FPlayer, CommandContext, Boolean> fPlayerFilter, BiFunction<Faction, CommandContext, Boolean> factionFilter){
        super(name, defaultValue, new AllFPlayerArgumentProvider(fPlayerFilter), new FactionTagArgumentProvider(factionFilter));
    }
}
