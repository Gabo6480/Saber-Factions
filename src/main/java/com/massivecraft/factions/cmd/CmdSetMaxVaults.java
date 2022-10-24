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

public class CmdSetMaxVaults extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     *  This command is used to set target faction's maximum vaults
     */

    public CmdSetMaxVaults() {
        this.aliases.addAll(Aliases.setMaxVaults);
        this.requiredArgs.put("faction", context -> Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList()));
        this.requiredArgs.put("amount", context -> {
            List<String> completions = new ArrayList<>();
            Faction faction = context.argAsFaction(0);
            if(faction == null)
                return null;

            String value = context.argAsString(1);
            try {
                if(Integer.parseInt(value) < 0)
                    return null;

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
