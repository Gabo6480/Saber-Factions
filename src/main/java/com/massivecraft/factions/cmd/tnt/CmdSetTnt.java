package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.FactionTagArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdSetTnt extends FCommand {

    /**
     * This command is used to set the amount of TNT stored in target faction's TNT Bank
     */

    public CmdSetTnt() {
        this.aliases.addAll(Aliases.setTnt);
        this.requiredArgs.add(new FactionTagArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("amount", (integer, context) -> integer > 0 && integer <= context.faction.getTntBankLimit()));

        this.requirements = new CommandRequirements.Builder(Permission.SET_TNT).build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction targetFac = context.argAsFaction(0);
        int value = context.argAsInt(1, -1);

        if (value < 0) {
            context.sender.sendMessage(TL.COMMAND_SETTNT_MUSTBE_GREATERTHANZERO.toString());
            return;
        }

        if (targetFac == null) {
            context.sender.sendMessage(TL.COMMAND_SETTNT_TARGETDOESNTEXIST.toString());
            return;
        }

        if (targetFac.isSystemFaction()) {
            context.sender.sendMessage(TL.COMMAND_SETTNT_CANTSETSYSTEM.toString());
            return;
        }

        if (value > targetFac.getTntBankLimit()) {
            context.sender.sendMessage(TL.COMMAND_SETTNT_MUSTBE_LESSTHANLIMIT.format(targetFac.getTntBankLimit()));
            return;
        }

        targetFac.setTnt(value);
        context.sender.sendMessage(TL.COMMAND_SETTNT_SUCCESS.format(targetFac.getTag(), value));


    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETTNT_DESCRIPTION;
    }
}
