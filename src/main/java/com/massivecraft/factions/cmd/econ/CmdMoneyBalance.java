package com.massivecraft.factions.cmd.econ;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.FactionTagArgumentProvider;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdMoneyBalance extends FCommand {

    /**
     * @author FactionsUUID Team
     */

    public CmdMoneyBalance() {
        super();
        this.aliases.addAll(Aliases.money_balance);

        //this.requiredArgs.add("");
        this.optionalArgs.add(new FactionTagArgumentProvider("faction", "yours"));

        this.setHelpShort(TL.COMMAND_MONEYBALANCE_SHORT.toString());

        this.requirements = new CommandRequirements.Builder(Permission.MONEY_BALANCE).build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction faction = context.faction;
        if (context.argIsSet(0)) {
            faction = context.argAsFaction(0);
        }

        if (faction == null) {
            return;
        }
        if (faction != context.faction && !Permission.MONEY_BALANCE_ANY.has(context.sender, true)) {
            return;
        }

        Econ.sendBalanceInfo(context.fPlayer, faction);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYBALANCE_DESCRIPTION;
    }

}