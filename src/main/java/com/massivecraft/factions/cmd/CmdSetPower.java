package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.cmd.core.args.OnlinePlayerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

/**
 * Factions - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 6/20/2020
 */
public class CmdSetPower extends FCommand {

    /**
     * This command is used to set the target player's current power
     */

    public CmdSetPower() {
        this.aliases.addAll(Aliases.setPower);
        this.requiredArgs.add(new OnlinePlayerArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("power", (integer, context) ->  {
            FPlayer targetPlayer = context.argAsFPlayer(0);
            if(targetPlayer == null)
                return false;
            return integer <= targetPlayer.getPowerMaxRounded();
        }));

        this.requirements = new CommandRequirements.Builder(Permission.SETPOWER)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer targetPlayer = context.argAsFPlayer(0);
        int value = context.argAsInt(1, -1);
        if (value < 0) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_MUSTBE_GREATERTHANZERO.toString());
            return;
        }

        if (targetPlayer == null) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_TARGETOFFLINE.toString());
            return;
        }

        if (value > targetPlayer.getPowerMaxRounded()) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_MUSTBE_LESSTHANMAXPOWER.toString());
            return;
        }

        if (targetPlayer.isAlt() && !FactionsPlugin.getInstance().getConfig().getBoolean("f-alts.Have-Power")) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_CANTBEALT.toString());
            return;
        }

        targetPlayer.setPowerRounded(value);
        context.sender.sendMessage(TL.COMMAND_SETPOWER_SUCCESS.format(targetPlayer.getName(), value));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETPOWER_DESCRIPTION;
    }

}
