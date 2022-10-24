package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factions - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 6/20/2020
 */
public class CmdSetPower extends FCommand {

    /**
     * This command is used to set the target player's current power
     */

    public CmdSetPower() {
        this.aliases.addAll(Aliases.setPower);
        this.requiredArgs.put("player", context -> FPlayers.getInstance().getOnlinePlayers().stream().map(FPlayer::getName).collect(Collectors.toList()));
        this.requiredArgs.put("power", context -> {
            List<String> completions = new ArrayList<>();
            FPlayer targetPlayer = context.argAsFPlayer(0);
            if(targetPlayer == null)
                return null;
            String value = context.argAsString(1);
            int maxPower = targetPlayer.getPowerMaxRounded();
            try {

                if(Integer.parseInt(value) <= maxPower)
                    for (int i = 0; i < 10; i++) {
                        String completeInt = value + i;
                        // Just to check if it can be parsed into a double
                        if(Integer.parseInt(completeInt) <= maxPower)
                            completions.add(completeInt);
                    }
            }
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }
            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.SETPOWER)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer targetPlayer = context.argAsFPlayer(0);
        int value = context.argAsInt(1, -1);
        if (value < 0) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_MUSTBE_GREATERTHANZERO.toString());
            return;
        }

        if (targetPlayer == null) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_TARGETOFFLINE.toString());
            return;
        }

        if (value > targetPlayer.getPowerMaxRounded()) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_MUSTBE_LESSTHANMAXPOWER.toString());
            return;
        }

        if (targetPlayer.isAlt() && !FactionsPlugin.getInstance().getConfig().getBoolean("f-alts.Have-Power")) {
            context.sender.sendMessage(TL.COMMAND_SETPOWER_CANTBEALT.toString());
            return;
        }

        targetPlayer.setPowerRounded(value);
        context.sender.sendMessage(TL.COMMAND_SETPOWER_SUCCESS.format(targetPlayer.getName(), value));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETPOWER_DESCRIPTION;
    }

}
