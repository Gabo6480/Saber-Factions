package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.cmd.core.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;


/**
 * Argument used to get all current online faction players.
 * If you DON'T require operating based on the target's faction then use OnlinePlayerArgumentProvider instead.
 */

public class AllFPlayerArgumentProvider extends AbstractArgumentProvider<FPlayer> {

    public AllFPlayerArgumentProvider(){
        super("player",null, (p, ctx) -> true);
    }

    public AllFPlayerArgumentProvider(@NotNull String name){
        super(name,null,(p, ctx) -> true);
    }

    public AllFPlayerArgumentProvider(BiFunction<FPlayer, CommandContext, Boolean> fPlayerFilter){
        super("player",null, fPlayerFilter);
    }
    public AllFPlayerArgumentProvider(@NotNull String name, String defaultValue, BiFunction<FPlayer, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
    }

    @Override
    public Collection<? extends FPlayer> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return FPlayers.getInstance().getAllFPlayers();
    }

    @Override
    public String getObjectName(FPlayer object) {
        return object.getName();
    }
}
