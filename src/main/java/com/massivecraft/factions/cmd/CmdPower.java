package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.OnlinePlayerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdPower extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */

    public CmdPower() {
        super();
        this.aliases.addAll(Aliases.power_power);
        this.optionalArgs.add(new OnlinePlayerArgumentProvider("player", "you"));

        this.requirements = new CommandRequirements.Builder(Permission.POWER)
                .build();

    }

    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsBestFPlayerMatch(0, context.fPlayer);
        if (target == null) {
            return;
        }

        if (target != context.fPlayer && !Permission.POWER_ANY.has(context.sender, true)) {
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!context.payForCommand(Conf.econCostPower, TL.COMMAND_POWER_TOSHOW, TL.COMMAND_POWER_FORSHOW)) {
            return;
        }


        double powerBoost = target.getPowerBoost();
        String boost = (powerBoost == 0.0) ? "" : (powerBoost > 0.0 ? TL.COMMAND_POWER_BONUS.toString() : TL.COMMAND_POWER_PENALTY.toString()) + powerBoost + ")";
        context.msg(TL.COMMAND_POWER_POWER, target.describeTo(context.fPlayer, true), target.getPowerRounded(), target.getPowerMaxRounded(), boost);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_POWER_DESCRIPTION;
    }

}
