package com.massivecraft.factions.cmd.econ;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.Aliases;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.cmd.audit.FLogType;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.List;


public class CmdMoneyWithdraw extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to withdraw money from optionally sender's current faction's or given target faction's account into the sender's account
     */

    public CmdMoneyWithdraw() {
        this.aliases.addAll(Aliases.money_withdraw);

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

        this.requirements = new CommandRequirements.Builder(Permission.MONEY_F2P)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        double amount = context.argAsDouble(0, 0d);

        if (amount <= 0) {
            return;
        }

        Faction faction = context.argAsFaction(1, context.faction);
        if (faction == null) {
            return;
        }

        Access access = context.faction.getAccess(context.fPlayer, PermissableAction.WITHDRAW);
        if (context.fPlayer.getRole() != Role.LEADER) {
            if (access == Access.DENY) {
                context.msg(TL.GENERIC_NOPERMISSION, "withdraw", "withdraw money from the bank");
                return;
            }
        }
        boolean success = Econ.transferMoney(context.fPlayer, faction, context.fPlayer, amount);

        if (success && Conf.logMoneyTransactions) {
            Logger.printArgs(TL.COMMAND_MONEYWITHDRAW_WITHDRAW.toString(), Logger.PrefixType.WARNING, context.fPlayer.getName(), Econ.moneyString(amount), faction.describeTo(null));
            FactionsPlugin.instance.logFactionEvent(faction, FLogType.BANK_EDIT, context.fPlayer.getName(), CC.RedB + "WITHDREW", amount + "");
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYWITHDRAW_DESCRIPTION;
    }
}
