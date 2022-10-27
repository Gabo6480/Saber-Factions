package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.core.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;

public class FactionMemberArgumentProvider extends AbstractArgumentProvider<FPlayer> {

    public FactionMemberArgumentProvider(){
        this("member",null, (p, ctx) -> true);
    }

    public FactionMemberArgumentProvider(@NotNull String name){
        this(name,null,(p, ctx) -> true);
    }

    public FactionMemberArgumentProvider(BiFunction<FPlayer, CommandContext, Boolean> MemberFilter){
        this("member",null, MemberFilter);
    }

    public FactionMemberArgumentProvider(@NotNull String name, String defaultValue, BiFunction<FPlayer, CommandContext, Boolean> filter) {
        super(name, defaultValue, filter);
    }

    @Override
    public Collection<? extends FPlayer> getObjectCollection(CommandContext context, int currentArgumentIndex) {
        return context.faction.getFPlayers();
    }

    @Override
    public String getObjectName(FPlayer object) {
        return object.getName();
    }
}
