package com.massivecraft.factions.cmd.econ;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.AllFPlayerArgumentProvider;
import com.massivecraft.factions.cmd.core.args.FactionTagArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.DoubleArgumentProvider;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CmdMoneyTransferFf extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to transfer money from given faction's account into the other given faction's account
     */

    public CmdMoneyTransferFf() {
        this.aliases.addAll(Aliases.money_transfer_Ff);

        this.requiredArgs.add(new DoubleArgumentProvider("amount", (number, context) -> number > 0));
        this.requiredArgs.add(new FactionTagArgumentProvider("from faction"));
        this.requiredArgs.add(new FactionTagArgumentProvider("to faction", (faction, context) -> {
            Faction target = context.argAsFaction(1, null, false);
            if(target != null)
                return !target.getId().equals(faction.getId());
            return false;
        } ));

        this.requirements = new CommandRequirements.Builder(Permission.MONEY_F2F).build();
    }

    @Override
    public void perform(CommandContext context) {
        double amount = context.argAsDouble(0, 0d);

        if (amount <= 0) {
            return;
        }


        EconomyParticipator from = context.argAsFaction(1);
        if (from == null) {
            return;
        }
        EconomyParticipator to = context.argAsFaction(2);
        if (to == null) {
            return;
        }

        boolean success = Econ.transferMoney(context.fPlayer, from, to, amount);

        if (success && Conf.logMoneyTransactions) {
            String name = context.sender instanceof Player ? context.fPlayer.getName() : context.sender.getName();
            Logger.printArgs(TL.COMMAND_MONEYTRANSFERFF_TRANSFER.toString(), Logger.PrefixType.DEFAULT, name, Econ.moneyString(amount), from.describeTo(null), to.describeTo(null));
        }
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYTRANSFERFF_DESCRIPTION;
    }
}