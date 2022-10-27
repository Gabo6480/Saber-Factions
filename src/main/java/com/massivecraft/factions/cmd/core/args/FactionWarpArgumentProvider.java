package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.struct.Warp;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FactionWarpArgumentProvider extends AbstractArgumentProvider<Warp>{

    public FactionWarpArgumentProvider(@NotNull String name, String defaultValue, BiFunction<Warp, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
    }

    public FactionWarpArgumentProvider(){
        super("warp",null, (w, ctx) -> true);
    }

    public FactionWarpArgumentProvider(@NotNull String name){
        super(name,null,(w, ctx) -> true);
    }

    public FactionWarpArgumentProvider(BiFunction<Warp, CommandContext, Boolean> warpFilter){
        super("warp",null, warpFilter);
    }

    @Override
    public Collection<? extends Warp> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return context.faction.getWarps();
    }

    @Override
    public String getObjectName(Warp object) {
        return object.getName();
    }
}
