package com.massivecraft.factions.cmd.check;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.Aliases;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.discord.Discord;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class CmdWeeWoo extends FCommand {

    /**
     * @author Vankka - Refactored by Gabo6480
     *
     * This command is used to send an alert to all members in sender's faction
     */

    public CmdWeeWoo() {
        this.aliases.addAll(Aliases.weewoo);
        this.requiredArgs.put("start|stop",context -> new ArrayList<String>(){{
            add("start");
            add("stop");
        }});

        this.requirements = new CommandRequirements.Builder(Permission.CHECK)
                .playerOnly()
                .memberOnly()
                .build();
    }

    public void perform(CommandContext context) {
        if (context.faction == null || !context.faction.isNormal()) {
            return;
        }
        String argument = context.argAsString(0).toLowerCase();
        boolean weewoo = context.faction.isWeeWoo();

        TL message;

        switch(argument){
            case "start":
            {
                if (weewoo) {
                    context.msg(TL.COMMAND_WEEWOO_ALREADY_STARTED);
                    return;
                }
                context.faction.setWeeWoo(true);
                message = TL.COMMAND_WEEWOO_STARTED;
            }
            break;
            case "stop":
            {
                if (!weewoo) {
                    context.msg(TL.COMMAND_WEEWOO_ALREADY_STOPPED);
                    return;
                }
                context.faction.setWeeWoo(false);
                message = TL.COMMAND_WEEWOO_STOPPED;
            }
            break;
            default:{
                context.msg("/f weewoo <start/stop>");
                return;
            }
        }

        context.msg(message, context.fPlayer.getNameAndTag());
        if (!FactionsPlugin.getInstance().getFileManager().getDiscord().fetchBoolean("Discord.useDiscordSystem"))
            return;
        String discordChannelId = context.faction.getWeeWooChannelId();
        if (discordChannelId != null && !discordChannelId.isEmpty()) {
            TextChannel textChannel = Discord.jda.getTextChannelById(discordChannelId);
            if (textChannel == null) {
                return;
            }
            if (!textChannel.getGuild().getSelfMember().hasPermission(textChannel, net.dv8tion.jda.api.Permission.MESSAGE_READ, net.dv8tion.jda.api.Permission.MESSAGE_WRITE)) {
                textChannel.getGuild().retrieveOwner().complete().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage((":x: Missing read/write in " + textChannel.getAsMention())).queue());
                return;
            }
            textChannel.sendMessage(message.format(context.fPlayer.getNameAndTag())).queue();
        }
    }

    public TL getUsageTranslation() {
        return TL.COMMAND_WEEWOO_DESCRIPTION;
    }
}
