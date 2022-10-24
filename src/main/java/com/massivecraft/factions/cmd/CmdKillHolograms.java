package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.List;

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
        this.requiredArgs.put("radius", context -> {
            List<String> completions = new ArrayList<>();
            String value = context.argAsString(0);
            try {
                // Just to check if it can be parsed into a double
                Integer.parseInt(value);
                for (int i = 0; i < 10; i++) {
                    String completeInt = value + i;
                    // Just to check if it can be parsed into a double
                    Integer.parseInt(completeInt);
                    completions.add(completeInt);
                }
            }
            catch (Exception ignored){
                if(completions.isEmpty())
                    return null;
            }

            return completions;
        });

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

