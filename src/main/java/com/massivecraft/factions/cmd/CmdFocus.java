package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.scoreboards.FTeamWrapper;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.List;

public class CmdFocus extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to indicate to the sender's current faction to focus a specific player
     */

    public CmdFocus() {
        aliases.addAll(Aliases.focus);

        requiredArgs.put("player", (context) -> {
            List<String> completions = new ArrayList<>();
            for (FPlayer player : FPlayers.getInstance().getOnlinePlayers()){
                if(player.getFaction().getId().equals(context.faction.getId()))
                    continue;

                completions.add(player.getName());
            }

            if(completions.isEmpty())
                return null;

            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.FOCUS)
                .playerOnly()
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.getInstance().getConfig().getBoolean("ffocus.Enabled")) {
            context.msg(TL.GENERIC_DISABLED, "Faction Focus");
            return;
        }
        FPlayer target = context.argAsFPlayer(0);
        if (target == null) {
            return;
        }

        if (target.getFaction().getId().equalsIgnoreCase(context.faction.getId())) {
            context.msg(TL.COMMAND_FOCUS_SAMEFACTION);
            return;
        }
        if ((context.faction.getFocused() != null) && (context.faction.getFocused().equalsIgnoreCase(target.getName()))) {
            context.faction.setFocused(null);
            context.faction.msg(TL.COMMAND_FOCUS_NO_LONGER, target.getName());
            FTeamWrapper.updatePrefixes(target.getFaction());
            return;
        }
        context.faction.msg(TL.COMMAND_FOCUS_FOCUSING, target.getName());
        context.faction.setFocused(target.getName());
        FTeamWrapper.updatePrefixes(target.getFaction());
    }

    public TL getUsageTranslation() {
        return TL.COMMAND_FOCUS_DESCRIPTION;
    }
}
