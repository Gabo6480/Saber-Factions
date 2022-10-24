package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import java.util.stream.Collectors;

public class CmdStrikesGive extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to increment the target faction's strike count by the given number
     */

    public CmdStrikesGive() {
        super();
        this.aliases.addAll(Aliases.strikes_give);
        this.requiredArgs.put("faction", context -> Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList()));
        this.optionalArgs.put("amount","number");

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
        target.setStrikes(target.getStrikes() + context.argAsInt(1, 1));
        context.msg(TL.COMMAND_STRIKES_CHANGED, target.getTag(), target.getStrikes());
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STRIKESGIVE_DESCRIPTION;
    }

}
