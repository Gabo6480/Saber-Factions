package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdPowerBoost extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to set a faction/player's power boost
     */

    public CmdPowerBoost() {
        super();
        this.aliases.addAll(Aliases.power_boost);
        this.requiredArgs.put("type", context -> new ArrayList<String>(){{
            add("plugin");
            add("player");
            add("faction");
            add("f");
        }});
        this.requiredArgs.put("target", context -> {
            String type = context.argAsString(0).toLowerCase();
            List<String> completions = new ArrayList<>();
            switch (type){
                case "f":
                case "faction":
                {
                    for (Faction faction : Factions.getInstance().getAllFactions()){
                        completions.add(faction.getTag());
                    }
                }
                break;
                case "player":
                case "plugin":
                {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) completions.add(player.getName());
                }
                break;
                default:
                    return null;
            }

            return completions;
        });
        this.requiredArgs.put("power|reset", context -> {
            List<String> completions = new ArrayList<>();

            completions.add("reset");
            String value = context.argAsString(2);
            try {
                // Just to check if it can be parsed into a double
                Double.parseDouble(value);
                if(!value.contains("."))
                completions.add(value + ".");
                for (int i = 0; i < 10; i++) {
                    String completeInt = value + i;
                    // Just to check if it can be parsed into a double
                    Double.parseDouble(completeInt);
                    completions.add(completeInt);
                }
            }
            catch (Exception ignored){}

            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.POWERBOOST)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String type = context.argAsString(0).toLowerCase();
        boolean doPlayer;
        switch (type){
            case "f":
            case "faction":
            {
                doPlayer = false;
            }
            break;
            case "player":
            case "plugin":
            {
                doPlayer = true;
            }
            break;
            default:
            {
                context.msg(TL.COMMAND_POWERBOOST_HELP_1);
                context.msg(TL.COMMAND_POWERBOOST_HELP_2);
                return;
            }
        }

        Double targetPower = context.argAsDouble(2);
        if (targetPower == null) {
            if (context.argAsString(2).equalsIgnoreCase("reset")) {
                targetPower = 0D;
            } else {
                context.msg(TL.COMMAND_POWERBOOST_INVALIDNUM);
                return;
            }
        }

        String target;

        if (doPlayer) {
            FPlayer targetPlayer = context.argAsBestFPlayerMatch(1);
            if (targetPlayer == null) {
                return;
            }

            if (targetPower != 0) {
                targetPower += targetPlayer.getPowerBoost();
            }
            targetPlayer.setPowerBoost(targetPower);
            target = TL.COMMAND_POWERBOOST_PLAYER.format(targetPlayer.getName());
        } else {
            Faction targetFaction = context.argAsFaction(1);
            if (targetFaction == null) {
                return;
            }

            if (targetPower != 0) {
                targetPower += targetFaction.getPowerBoost();
            }
            targetFaction.setPowerBoost(targetPower);
            target = TL.COMMAND_POWERBOOST_FACTION.format(targetFaction.getTag());
        }

        int roundedPower = (int) Math.round(targetPower);
        context.msg(TL.COMMAND_POWERBOOST_BOOST, target, roundedPower);
        if (!(context.sender instanceof ConsoleCommandSender)) {
            Logger.printArgs(TL.COMMAND_POWERBOOST_BOOSTLOG.toString(), Logger.PrefixType.DEFAULT, context.fPlayer.getName(), target, roundedPower);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_POWERBOOST_DESCRIPTION;
    }
}
