package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.WorldArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class CmdSafeunclaimall extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */

    public CmdSafeunclaimall() {
        this.aliases.addAll(Aliases.unclaim_all_safe);
        this.optionalArgs.add(new WorldArgumentProvider("world","all"));

        this.requirements = new CommandRequirements.Builder(Permission.MANAGE_SAFE_ZONE)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FactionsPlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(FactionsPlugin.instance, () -> {


            String worldName = context.argAsString(0);
            World world = null;

            if (worldName != null) {
                world = Bukkit.getWorld(worldName);
            }

            String id = Factions.getInstance().getSafeZone().getId();

            if (world == null) {
                Board.getInstance().unclaimAll(id);
            } else {
                Board.getInstance().unclaimAllInWorld(id, world);
            }

            context.msg(TL.COMMAND_SAFEUNCLAIMALL_UNCLAIMED);

            if (Conf.logLandUnclaims) {
                Logger.print(TL.COMMAND_SAFEUNCLAIMALL_UNCLAIMEDLOG.format(context.sender.getName()), Logger.PrefixType.DEFAULT);
            }
        });
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SAFEUNCLAIMALL_DESCRIPTION;
    }

}
