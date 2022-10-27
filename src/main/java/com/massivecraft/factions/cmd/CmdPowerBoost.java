package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.CompositeArgumentProvider;
import com.massivecraft.factions.cmd.core.args.FPlayerAndFactionArgumentProvider;
import com.massivecraft.factions.cmd.core.args.ListStringArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.DoubleArgumentProvider;
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
        this.requiredArgs.add(new ListStringArgumentProvider("type", null, "player","plugin","faction","f"));
        this.requiredArgs.add(new FPlayerAndFactionArgumentProvider("target", null,
                (fPlayer, context) -> context.argAsString(0).equalsIgnoreCase("player") || context.argAsString(0).equalsIgnoreCase("plugin"),
                (faction, context) -> context.argAsString(0).equalsIgnoreCase("faction") || context.argAsString(0).equalsIgnoreCase("f")));
        this.requiredArgs.add(new CompositeArgumentProvider(new DoubleArgumentProvider("power"), new ListStringArgumentProvider("reset", null, "reset")));

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
