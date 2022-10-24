package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdSetDefaultRole extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     *
     * This command is used to set the role that players get as default when joining the sender's current faction
     */

    public CmdSetDefaultRole() {
        super();

        this.aliases.addAll(Aliases.setDefaultRole);
        this.requiredArgs.put("role", context -> {
            List<String> completions = new ArrayList<>();
            for (Role role: Role.values()) {
                if(role == Role.LEADER)
                    continue;

                completions.addAll(Arrays.stream(role.aliases).collect(Collectors.toList()));
            }

            return completions;
        });

        this.requirements = new CommandRequirements.Builder(Permission.DEFAULTRANK)
                .playerOnly()
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Role target = Role.fromString(context.argAsString(0));
        if (target == null) {
            context.msg(TL.COMMAND_SETDEFAULTROLE_INVALIDROLE, context.argAsString(0));
            return;
        }

        if (target == Role.LEADER) {
            context.msg(TL.COMMAND_SETDEFAULTROLE_NOTTHATROLE, context.argAsString(0));
            return;
        }


        context.faction.setDefaultRole(target);
        context.msg(TL.COMMAND_SETDEFAULTROLE_SUCCESS, target.displayName);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETDEFAULTROLE_DESCRIPTION;
    }
}
