package com.massivecraft.factions.cmd.points;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.FPlayerAndFactionArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
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

        this.requiredArgs.add( new FPlayerAndFactionArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("points", (numb, context) -> {
            if(numb < 0)
                return false;

            Faction faction;
            FPlayer fPlayer = context.argAsFPlayer(0, null, false);
            if (fPlayer != null) faction = fPlayer.getFaction();
            else faction = Factions.getInstance().getByTag(context.args.get(0));

            if(faction != null){
                int points = faction.getPoints();

                return points <= numb;
            }

            return false;
        }));

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
