package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.FactionTagArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdSetMaxVaults extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     *  This command is used to set target faction's maximum vaults
     */

    public CmdSetMaxVaults() {
        this.aliases.addAll(Aliases.setMaxVaults);
        this.requiredArgs.add(new FactionTagArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("amount",
                (numb, context) -> context.argAsFaction(0) != null && numb >= 0));

        this.requirements = new CommandRequirements.Builder(Permission.SETMAXVAULTS)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction targetFaction = context.argAsFaction(0);
        int value = context.argAsInt(1, -1);
        if (value < 0) {
            context.sender.sendMessage(TL.COMMAND_SETMAXVAULTS_MUSTBE_GREATERTHANZERO.toString());
            return;
        }

        if (targetFaction == null) {
            context.sender.sendMessage(TL.COMMAND_SETMAXVAULTS_COULDNTFINDTARGET.format(context.argAsString(0)));
            return;
        }

        targetFaction.setMaxVaults(value);
        context.sender.sendMessage(TL.COMMAND_SETMAXVAULTS_SUCCESS.format(targetFaction.getTag(), value));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETMAXVAULTS_DESCRIPTION;
    }
}
