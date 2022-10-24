package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
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

public class CmdPermanentPower extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to set a faction's permanent power
     */

    public CmdPermanentPower() {
        super();
        this.aliases.addAll(Aliases.permanent_power);

        this.requiredArgs.put("faction", context -> {
            List<String> completions = new ArrayList<>();
            for (Faction faction : Factions.getInstance().getAllFactions()){
                completions.add(faction.getTag());
            }
            return completions;
        });

        this.requiredArgs.put("power", context -> {
            List<String> completions = new ArrayList<>();
            String value = context.argAsString(1);
            try{
                // Just to check if it can be parsed into an int
                Integer.parseInt(value);

                for (int i = 0; i < 10; i++) {
                    String completeInt = value + i;
                    // Just to check if it can be parsed into an int
                    Integer.parseInt(completeInt);
                    completions.add(completeInt);
                }
            }
            catch (NumberFormatException ex) {
                if(completions.isEmpty())
                    return null;
            }
            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.SET_PERMANENTPOWER)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction targetFaction = context.argAsFaction(0);
        if (targetFaction == null) {
            return;
        }

        Integer targetPower = context.argAsInt(1);

        targetFaction.setPermanentPower(targetPower);

        String change = TL.COMMAND_PERMANENTPOWER_REVOKE.toString();
        if (targetFaction.hasPermanentPower()) {
            change = TL.COMMAND_PERMANENTPOWER_GRANT.toString();
        }

        // Inform sender
        context.msg(TL.COMMAND_PERMANENTPOWER_SUCCESS, change, targetFaction.describeTo(context.fPlayer));

        // Inform all other players
        for (FPlayer fplayer : targetFaction.getFPlayersWhereOnline(true)) {
            if (fplayer == context.fPlayer) {
                continue;
            }
            String blame = (context.fPlayer == null ? TL.GENERIC_SERVERADMIN.toString() : context.fPlayer.describeTo(fplayer, true));
            fplayer.msg(TL.COMMAND_PERMANENTPOWER_FACTION, blame, change);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_PERMANENTPOWER_DESCRIPTION;
    }
}
