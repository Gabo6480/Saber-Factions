package com.massivecraft.factions.cmd.econ;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.audit.FLogType;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;


public class CmdMoneyDeposit extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to deposit money from sender's account into optionally their faction or given target faction
     */

    public CmdMoneyDeposit() {
        super();
        this.aliases.addAll(Aliases.money_deposit);

        this.requiredArgs.put("amount", context -> {
            List<String> completions = new ArrayList<>();
            String value = context.argAsString(0);
            try{
                // Just to check if it can be parsed into a double
                Double.parseDouble(value);
                if(!value.contains("."))
                completions.add(value + ".");
                for (int i = 0; i < 10; i++) {
                    String completeInt = value + i;
                    // Just to check if it can be parsed into a double
                    Double.parseDouble(completeInt);
                    completions.add(completeInt);
                }
            }
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }
            return completions;
        });

        this.optionalArgs.put("faction", "yours");

        this.requirements = new CommandRequirements.Builder(Permission.MONEY_DEPOSIT)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        double amount = context.argAsDouble(0, 0d);
        EconomyParticipator faction = context.argAsFaction(1, context.faction);

        if (amount <= 0) {
            return;
        }

        if (faction == null) {
            return;
        }
        boolean success = Econ.transferMoney(context.fPlayer, context.fPlayer, faction, amount);

        if (success && Conf.logMoneyTransactions) {
            Logger.printArgs(TL.COMMAND_MONEYDEPOSIT_DEPOSITED.toString(), Logger.PrefixType.DEFAULT, context.fPlayer.getName(), Econ.moneyString(amount), faction.describeTo(null));
            FactionsPlugin.instance.logFactionEvent(context.faction, FLogType.BANK_EDIT, context.fPlayer.getName(), ChatColor.GREEN + ChatColor.BOLD.toString() + "DEPOSITED", amount + "");

        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYDEPOSIT_DESCRIPTION;
    }

}

