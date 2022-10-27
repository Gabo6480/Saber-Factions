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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CmdMoneyTransferPf extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to transfer money from given player's account into the given faction's account
     */

    public CmdMoneyTransferPf() {
        this.aliases.addAll(Aliases.money_transfer_Pf);

        this.requiredArgs.add(new DoubleArgumentProvider("amount", (number, context) -> number > 0));
        this.requiredArgs.add(new AllFPlayerArgumentProvider("from player"));
        this.requiredArgs.add(new FactionTagArgumentProvider("to faction"));


        this.requirements = new CommandRequirements.Builder(Permission.MONEY_P2F).build();
    }

    @Override
    public void perform(CommandContext context) {
        double amount = context.argAsDouble(0, 0d);

        if (amount <= 0) {
            return;
        }

        EconomyParticipator from = context.argAsBestFPlayerMatch(1);
        if (from == null) {
            return;
        }
        EconomyParticipator to = context.argAsFaction(2);
        if (to == null) {
            return;
        }

        boolean success = Econ.transferMoney(context.fPlayer, from, to, amount);

        if (success && Conf.logMoneyTransactions) {
            Logger.printArgs(TL.COMMAND_MONEYTRANSFERPF_TRANSFER.toString(), Logger.PrefixType.WARNING, context.fPlayer.getName(), Econ.moneyString(amount), from.describeTo(null), to.describeTo(null));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYTRANSFERPF_DESCRIPTION;
    }
}
