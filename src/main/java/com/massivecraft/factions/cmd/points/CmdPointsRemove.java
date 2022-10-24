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

public class CmdPointsRemove extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to remove the given amount of points from the target faction or target player's faction
     */

    public CmdPointsRemove() {
        super();
        this.aliases.addAll(Aliases.points_remove);

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
            int points = faction.getPoints();
            try {
                int intValue = Integer.parseInt(value);
                if( intValue <= points && intValue > 0)
                    for (int i = 0; i < 10; i++) {
                        String completeInt = value + i;
                        // Just to check if it can be parsed into a double
                        if(Integer.parseInt(completeInt) <= points)
                            completions.add(completeInt);
                    }
            }
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }
            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.REMOVEPOINTS)
                .build();

    }


    @Override
    public void perform(CommandContext context) {
        Faction faction;

        FPlayer fPlayer = context.argAsFPlayer(0);
        if (fPlayer != null) {
            faction = fPlayer.getFaction();
        }
        else
            faction = Factions.getInstance().getByTag(context.args.get(0));

        if (faction == null || faction.isWilderness()) {
            context.msg(TL.COMMAND_POINTS_FAILURE.toString().replace("{faction}", context.args.get(0)));
            return;
        }

        if (context.argAsInt(1) <= 0) {
            context.msg(TL.COMMAND_POINTS_INSUFFICIENT);
            return;
        }

        faction.setPoints(faction.getPoints() - context.argAsInt(1));
        context.msg(TL.COMMAND_REMOVEPOINTS_SUCCESSFUL, context.argAsInt(1), faction.getTag(), faction.getPoints());
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_REMOVEPOINTS_DESCRIPTION;
    }


}
