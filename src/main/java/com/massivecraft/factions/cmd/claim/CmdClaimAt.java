package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.audit.FLogType;
import com.massivecraft.factions.cmd.core.args.CustomArgumentProvider;
import com.massivecraft.factions.cmd.core.args.WorldArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class CmdClaimAt extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to claim a chunk for the sender's faction at given location
     */

    public CmdClaimAt() {
        super();
        this.aliases.addAll(Aliases.claim_at);

        this.requiredArgs.add(new WorldArgumentProvider());
        this.requiredArgs.add(new IntegerArgumentProvider("x"));
        this.requiredArgs.add(new IntegerArgumentProvider("z"));

        this.requirements = new CommandRequirements.Builder(Permission.CLAIMAT)
                .playerOnly()
                .memberOnly()
                .withAction(PermissableAction.TERRITORY)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        int x = context.argAsInt(1);
        int z = context.argAsInt(2);
        FLocation location = new FLocation(context.argAsString(0), x, z);

        Faction at = Board.getInstance().getFactionAt(new FLocation(context.fPlayer.getPlayer().getLocation()));

        if (FactionsPlugin.cachedRadiusClaim && context.fPlayer.attemptClaim(context.fPlayer.getFaction(), context.player.getLocation(), true)) {
            context.fPlayer.getFaction().getFPlayersWhereOnline(true).forEach(f -> f.msg(TL.CLAIM_CLAIMED, context.fPlayer.describeTo(f, true), context.fPlayer.getFaction().describeTo(f), at.describeTo(f)));
            FactionsPlugin.instance.logFactionEvent(context.fPlayer.getFaction(), FLogType.CHUNK_CLAIMS, context.fPlayer.getName(), CC.GreenB + "CLAIMED", "1", (new FLocation(context.fPlayer.getPlayer().getLocation())).formatXAndZ(","));
            showMap(context);
            return;
        }
        context.fPlayer.attemptClaim(context.faction, location, true);
        FactionsPlugin.instance.logFactionEvent(context.fPlayer.getFaction(), FLogType.CHUNK_CLAIMS, context.fPlayer.getName(), CC.GreenB + "CLAIMED", "1", (location).formatXAndZ(","));
        showMap(context);
    }

    public void showMap(CommandContext context) {
        context.sendFancyMessage(Board.getInstance().getMap(context.fPlayer, new FLocation(context.fPlayer), context.player.getLocation().getYaw()));
    }


    @Override
    public TL getUsageTranslation() {
        return null;
    }
}
