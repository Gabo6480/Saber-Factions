package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.SingleWordArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Warp;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;

public class CmdSetFWarp extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to create a warp point at the sender's location
     */

    public CmdSetFWarp() {
        this.aliases.addAll(Aliases.setWarp);
        this.requiredArgs.add(new SingleWordArgumentProvider("warp name", (warp, context) -> !context.faction.isWarp(warp)));
        this.optionalArgs.add(new SingleWordArgumentProvider("password"));
        this.requirements = new CommandRequirements.Builder(Permission.SETWARP).playerOnly().memberOnly().withAction(PermissableAction.SETWARP).build();
    }

    @Override
    public void perform(CommandContext context) {
        if (context.fPlayer.getRelationToLocation() != Relation.MEMBER) {
            context.msg(TL.COMMAND_SETFWARP_NOTCLAIMED);
            return;
        }

        String warpName = context.argAsString(0);
        boolean warpExists = context.faction.isWarp(warpName);
        int maxWarps = context.faction.getWarpsLimit();
        boolean tooManyWarps = maxWarps <= context.faction.getWarps().size();
        if (tooManyWarps && !warpExists) {
            context.msg(TL.COMMAND_SETFWARP_LIMIT, maxWarps);
            return;
        }
        if (!this.transact(context.fPlayer, context)) {
            return;
        }
        String password = context.argAsString(1);
        Warp newWarp = context.faction.setWarp(warpName, context.player.getLocation());
        if (password != null) {
            newWarp.setPassword(password);
        }
        context.msg(TL.COMMAND_SETFWARP_SET, warpName, (password != null) ? password : "");
    }

    private boolean transact(FPlayer player, CommandContext context) {
        return !FactionsPlugin.getInstance().getConfig().getBoolean("warp-cost.enabled", false) || player.isAdminBypassing() || context.payForCommand(FactionsPlugin.getInstance().getConfig().getDouble("warp-cost.setwarp", 5.0), TL.COMMAND_SETFWARP_TOSET.toString(), TL.COMMAND_SETFWARP_FORSET.toString());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETFWARP_DESCRIPTION;
    }
}

