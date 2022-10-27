package com.massivecraft.factions.cmd.points;

import com.massivecraft.factions.FPlayer;
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

public class CmdPointsSet extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to set the target faction's ,or target player's faction's, Faction Point count to the given amount
     */

    public CmdPointsSet() {
        super();
        this.aliases.addAll(Aliases.points_set);

        this.requiredArgs.add(new FPlayerAndFactionArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("points", (numb, context) -> {
            if(numb < 0)
                return false;

            Faction faction;
            FPlayer fPlayer = context.argAsFPlayer(0, null, false);
            if (fPlayer != null) faction = fPlayer.getFaction();
            else faction = Factions.getInstance().getByTag(context.args.get(0));

            return faction != null;
        }));


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
