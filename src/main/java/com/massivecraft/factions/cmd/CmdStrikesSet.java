package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdStrikesSet extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to set target faction's strike count
     */

    public CmdStrikesSet() {
        super();
        this.aliases.addAll(Aliases.strikes_set);
        this.requiredArgs.put("faction", context -> Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList()));
        this.requiredArgs.put("amount", context -> {
            List<String> completions = new ArrayList<>();
            String value = context.argAsString(1);
            try {

                Integer.parseInt(value) ;
                    for (int i = 0; i < 10; i++) {
                        String completeInt = value + i;
                        // Just to check if it can be parsed into a double
                        Integer.parseInt(completeInt);
                            completions.add(completeInt);
                    }
            }
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }

            return completions;
        });

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
