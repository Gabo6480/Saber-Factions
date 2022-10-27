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

public class CmdStrikesSet extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to set target faction's strike count
     */

    public CmdStrikesSet() {
        super();
        this.aliases.addAll(Aliases.strikes_set);
        this.requiredArgs.add(new FactionTagArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("amount"));

        this.requirements = new CommandRequirements.Builder(Permission.SETSTRIKES)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction target = context.argAsFaction(0);
        if (target == null || target.isSystemFaction()) {
            context.msg(TL.COMMAND_STRIKES_TARGET_INVALID, context.argAsString(0));
            return;
        }
        target.setStrikes(context.argAsInt(1));
        context.msg(TL.COMMAND_STRIKES_CHANGED, target.getTag(), target.getStrikes());
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STRIKESET_DESCRIPTION;
    }

}
