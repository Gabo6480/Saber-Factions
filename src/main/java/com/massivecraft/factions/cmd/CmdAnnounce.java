package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.core.*;
import com.massivecraft.factions.cmd.core.args.SingleWordArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CmdAnnounce extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to send an announcement inside the sender's faction
     */

    public CmdAnnounce() {
        super();
        this.aliases.addAll(Aliases.announce);

        // TODO: Create an ArgumentProvider that works for strings with spaces
        this.requiredArgs.add(new SingleWordArgumentProvider("message"));

        this.requirements = new CommandRequirements.Builder(Permission.ANNOUNCE)
                .playerOnly()
                .memberOnly()
                .noErrorOnManyArgs()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String prefix = ChatColor.GREEN + context.faction.getTag() + ChatColor.YELLOW + " [" + ChatColor.GRAY + context.player.getName() + ChatColor.YELLOW + "] " + ChatColor.RESET;
        String message = StringUtils.join(context.args, " ");

        for (Player player : context.faction.getOnlinePlayers()) {
            player.sendMessage(prefix + message);
        }

        // Add for offline players.
        for (FPlayer fp : context.faction.getFPlayersWhereOnline(false)) {
            context.faction.addAnnouncement(fp, prefix + message);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_ANNOUNCE_DESCRIPTION;
    }



}