package com.massivecraft.factions.cmd.reserve;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Saser
 *
 * This command is used to reserve a specific faction tag for the given player
 */

public class CmdReserve extends FCommand {

    public CmdReserve() {
        this.aliases.addAll(Aliases.reserve);
        this.requiredArgs.put("faction tag" ,context -> new ArrayList<String>(){{
            add("[<faction tag>]");
        }});
        this.requiredArgs.put("player", context -> FPlayers.getInstance().getAllFPlayers().stream().map(FPlayer::getName).collect(Collectors.toList()));
        this.requirements = new CommandRequirements.Builder(
                Permission.RESERVE).build();
    }

    @Override
    public void perform(CommandContext context) {
        String target = context.argAsString(0);
        ReserveObject reserve = FactionsPlugin.getInstance().getFactionReserves().stream().filter(faction -> faction.getFactionName().equalsIgnoreCase(target)).findFirst().orElse(null);
        if (reserve != null) {
            context.msg(TL.COMMAND_RESERVE_ALREADYRESERVED, context.argAsString(0));
            return;
        }
        context.msg(TL.COMMAND_RESERVE_SUCCESS, context.argAsString(0), context.argAsString(1));
        FactionsPlugin.getInstance().getFactionReserves().add(new ReserveObject(context.argAsString(1), context.argAsString(0)));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RESERVE_DESCRIPTION;
    }
}
