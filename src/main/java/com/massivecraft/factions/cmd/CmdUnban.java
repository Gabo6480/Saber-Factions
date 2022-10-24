package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.List;

public class CmdUnban extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to unban a player from the sender's current faction
     */

    public CmdUnban() {
        super();
        this.aliases.addAll(Aliases.unban);
        this.requiredArgs.put("player", context -> {
            List<String> completions = new ArrayList<>();

            for(BanInfo banInfo: context.faction.getBannedPlayers()){
                FPlayer player = FPlayers.getInstance().getById(banInfo.getBanned());
                if(player != null)
                    completions.add(player.getName());
            }

            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.BAN)
                .playerOnly()
                .memberOnly()
                .withAction(PermissableAction.BAN)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        // Good on permission checks. Now lets just ban the player.
        FPlayer target = context.argAsFPlayer(0);
        if (target == null) {
            return; // the above method sends a message if fails to find someone.
        }

        // TODO Why is this check here?
        if (target.getFaction() != context.fPlayer.getFaction()) {
            if (target.getFaction().getAccess(context.fPlayer, PermissableAction.BAN) != Access.ALLOW) {
                if (!context.fPlayer.isAdminBypassing()) {
                    context.fPlayer.msg(TL.COMMAND_UNBAN_TARGET_IN_OTHER_FACTION, target.getName());
                }
            }
        }

        if (!context.faction.isBanned(target)) {
            context.msg(TL.COMMAND_UNBAN_NOTBANNED, target.getName());
            return;
        }

        context.faction.unban(target);

        context.msg(TL.COMMAND_UNBAN_UNBANNED, context.fPlayer.getName(), target.getName());
        target.msg(TL.COMMAND_UNBAN_TARGETUNBANNED, context.faction.getTag(target));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UNBAN_DESCRIPTION;
    }
}
