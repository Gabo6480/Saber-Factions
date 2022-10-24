package com.massivecraft.factions.cmd.points;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
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

public class CmdPointsSet extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to set the target faction's ,or target player's faction's, Faction Point count to the given amount
     */

    public CmdPointsSet() {
        super();
        this.aliases.addAll(Aliases.points_set);

        this.requiredArgs.put("target", context -> {
            List<String> completions = Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList());

            completions.addAll(FPlayers.getInstance().getAllFPlayers().stream().map(FPlayer::getName).collect(Collectors.toList()));

            return completions;
        });
        this.requiredArgs.put("points", context -> {
            List<String> completions = new ArrayList<>();
            Faction faction;

            FPlayer fPlayer = context.argAsFPlayer(0, null, false);
            if (fPlayer != null) {
                faction = fPlayer.getFaction();
            }
            else
                faction = Factions.getInstance().getByTag(context.args.get(0));
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


        this.requirements = new CommandRequirements.Builder(Permission.SETPOINTS)
                .build();
    }


    @Override
    public void perform(CommandContext context) {
        Faction faction = Factions.getInstance().getByTag(context.args.get(0));

        FPlayer fPlayer = context.argAsFPlayer(0);
        if (fPlayer != null) {
            faction = fPlayer.getFaction();
        }

        if (faction == null || faction.isWilderness()) {
            context.msg(TL.COMMAND_POINTS_FAILURE.toString().replace("{faction}", context.args.get(0)));
            return;
        }

        if (context.argAsInt(1) < 0) {
            context.msg(TL.COMMAND_POINTS_INSUFFICIENT);
            return;
        }

        faction.setPoints(context.argAsInt(1));
        context.msg(TL.COMMAND_SETPOINTS_SUCCESSFUL, context.argAsInt(1), faction.getTag(), faction.getPoints());
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETPOINTS_DESCRIPTION;
    }


}
