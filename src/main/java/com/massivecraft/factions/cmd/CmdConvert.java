package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Conf.Backend;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.persist.json.FactionsJSON;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;

public class CmdConvert extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to convert the plugin's backend settings
     */

    public CmdConvert() {
        this.aliases.addAll(Aliases.convert);
        this.requiredArgs.put("backend", context -> new ArrayList<String>(){{
            //add("MYSQL"); TODO add MySQL storage
            add("JSON");
        }});

        this.requirements = new CommandRequirements.Builder(Permission.CONVERT)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!(context.sender instanceof ConsoleCommandSender)) {
            context.sender.sendMessage(TL.GENERIC_CONSOLEONLY.toString());
        }
        Backend nb = Backend.valueOf(context.argAsString(0).toUpperCase());
        if (nb == Conf.backEnd) {
            context.sender.sendMessage(TL.COMMAND_CONVERT_BACKEND_RUNNING.toString());
            return;
        }
        if (nb == Backend.JSON) {
            FactionsJSON.convertTo();
        } else {
            context.sender.sendMessage(TL.COMMAND_CONVERT_BACKEND_INVALID.toString());
            return;
        }
        Conf.backEnd = nb;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CONVERT_DESCRIPTION;
    }

}