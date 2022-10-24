package com.massivecraft.factions.cmd;

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

public class CmdPeaceful extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to set a faction as a peaceful faction
     */

    public CmdPeaceful() {
        super();
        this.aliases.addAll(Aliases.peaceful);
        this.requiredArgs.put("faction", context -> {
            List<String> completions = new ArrayList<>();
            for (Faction faction : Factions.getInstance().getAllFactions()){
                completions.add(faction.getTag());
            }

            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.SET_PEACEFUL)
                .build();

    }

    @Override
    public void perform(CommandContext context) {
        Faction faction = context.argAsFaction(0);
        if (faction == null) {
            return;
        }

        String change;
        if (faction.isPeaceful()) {
            change = TL.COMMAND_PEACEFUL_REVOKE.toString();
            faction.setPeaceful(false);
        } else {
            change = TL.COMMAND_PEACEFUL_GRANT.toString();
            faction.setPeaceful(true);
        }

        // Inform all players
        for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
            String blame = (context.fPlayer == null ? TL.GENERIC_SERVERADMIN.toString() : context.fPlayer.describeTo(fplayer, true));
            if (fplayer.getFaction() == faction) {
                fplayer.msg(TL.COMMAND_PEACEFUL_YOURS, blame, change);
            } else {
                fplayer.msg(TL.COMMAND_PEACEFUL_OTHER, blame, change, faction.getTag(fplayer));
            }
        }

    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_PEACEFUL_DESCRIPTION;
    }

}
