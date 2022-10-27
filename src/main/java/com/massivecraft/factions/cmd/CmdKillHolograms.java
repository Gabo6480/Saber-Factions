package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

@Deprecated
public class CmdKillHolograms extends FCommand {

    /**
     * @author Illyria Team
     *
     * This command is used to make the sender use a command that kills all Armor Stands within a radius.
     */

    public CmdKillHolograms() {
        super();
        this.aliases.addAll(Aliases.killholograms);
        this.requiredArgs.add(new IntegerArgumentProvider("radius", (numb, context) -> numb >= 0));

        this.requirements = new CommandRequirements.Builder(Permission.KILLHOLOS)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        context.player.sendMessage("Killing Invisible Armor Stands..");
        context.player.chat("/minecraft:kill @e[type=ArmorStand,r=" + context.argAsInt(0) + "]");
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_KILLHOLOGRAMS_DESCRIPTION;
    }
}

