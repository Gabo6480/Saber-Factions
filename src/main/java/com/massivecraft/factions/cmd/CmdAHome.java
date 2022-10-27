package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.OnlineFPlayerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.stream.Collectors;

public class CmdAHome extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to teleport the given player to their faction home
     */

    public CmdAHome() {
        super();
        this.aliases.addAll(Aliases.ahome);

        this.requiredArgs.add(new OnlineFPlayerArgumentProvider((fPlayer, context) -> fPlayer.hasFaction() && fPlayer.getFaction().hasHome()));

        this.requirements = new CommandRequirements.Builder(Permission.AHOME).noDisableOnLock().build();
    }


    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsBestFPlayerMatch(0);
        if (target == null) {
            context.msg(TL.GENERIC_NOPLAYERMATCH, context.argAsString(0));
            return;
        }

        if (target.isOnline()) {
            Faction faction = target.getFaction();
            if (faction.hasHome()) {
                target.getPlayer().teleport(faction.getHome(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                context.msg(TL.COMMAND_AHOME_SUCCESS, target.getName());
                target.msg(TL.COMMAND_AHOME_TARGET);
            } else {
                context.msg(TL.COMMAND_AHOME_NOHOME, target.getName());
            }
        } else {
            context.msg(TL.COMMAND_AHOME_OFFLINE, target.getName());
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_AHOME_DESCRIPTION;
    }
}