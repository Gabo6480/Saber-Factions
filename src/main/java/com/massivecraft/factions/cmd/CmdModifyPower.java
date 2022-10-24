package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdModifyPower extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to modify target player's power by the given ammount
     */

    public CmdModifyPower() {
        super();

        this.aliases.addAll(Aliases.modifyPower);

        this.requiredArgs.put("player", context -> Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        this.requiredArgs.put("power", context -> {
            List<String> completions = new ArrayList<>();
            String value = context.argAsString(1);
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
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }

            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.MODIFY_POWER)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        // /f modify <name> #
        FPlayer player = context.argAsBestFPlayerMatch(0);
        Double number = context.argAsDouble(1); // returns null if not a Double.

        if (player == null || number == null) {
            context.sender.sendMessage(getHelpShort());
            return;
        }

        player.alterPower(number);
        int newPower = player.getPowerRounded(); // int so we don't have super long doubles.
        context.msg(TL.COMMAND_MODIFYPOWER_ADDED, number, player.getName(), newPower);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MODIFYPOWER_DESCRIPTION;
    }
}
