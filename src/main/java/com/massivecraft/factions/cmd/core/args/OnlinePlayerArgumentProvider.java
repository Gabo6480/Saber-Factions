package com.massivecraft.factions.cmd.core.args;


import com.massivecraft.factions.cmd.core.CommandContext;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * Argument used to get all current online players.
 * If you require operating based on the target's faction then use OnlineFPlayerArgumentProvider instead.
 */

public class OnlinePlayerArgumentProvider extends AbstractArgumentProvider<Player> {

    public OnlinePlayerArgumentProvider(){
        super("player",null, (p, ctx) -> true);
    }

    public OnlinePlayerArgumentProvider(@NotNull String name){
        super(name,null,(p, ctx) -> true);
    }

    public OnlinePlayerArgumentProvider(@NotNull String name, String defaultValue){
        super(name,defaultValue,(p, ctx) -> true);
    }

    public OnlinePlayerArgumentProvider(BiFunction<Player, CommandContext, Boolean> playerFilter){
        super("player",null, playerFilter);
    }
    public OnlinePlayerArgumentProvider(@NotNull String name, String defaultValue, BiFunction<Player, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
    }

    @Override
    public Collection<? extends Player> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public String getObjectName(Player object) {
        return object.getName();
    }
}
