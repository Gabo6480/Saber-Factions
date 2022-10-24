package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.Aliases;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdSetTnt extends FCommand {

    /**
     * This command is used to set the amount of TNT stored in target faction's TNT Bank
     */

    public CmdSetTnt() {
        this.aliases.addAll(Aliases.setTnt);
        this.requiredArgs.put("faction", context -> Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList()));
        this.requiredArgs.put("amount", context -> {
            List<String> completions = new ArrayList<>();
            String value = context.argAsString(1);
            int maxTnt = context.faction.getTntBankLimit();
            try {

                if(maxTnt >= 0 && Integer.parseInt(value) <= maxTnt)
                    for (int i = 0; i < 10; i++) {
                        String completeInt = value + i;
                        // Just to check if it can be parsed into a double
                        if(Integer.parseInt(completeInt) <= maxTnt)
                            completions.add(completeInt);
                    }
            }
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }

            return completions;
        });

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
