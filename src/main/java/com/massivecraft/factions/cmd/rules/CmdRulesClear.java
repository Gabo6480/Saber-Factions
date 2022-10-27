package com.massivecraft.factions.cmd.rules;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdRulesClear extends FCommand {

    /**
     * @author Illyria Team
     *
     * This command is used to remove all rules from the sender's current facction's custom rules text
     */

    public CmdRulesClear() {
        super();
        aliases.addAll(Aliases.rules_clear);

        this.requirements = new CommandRequirements.Builder(Permission.RULES)
                .playerOnly()
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.getInstance().getConfig().getBoolean("frules.Enabled")) {
            context.msg(TL.COMMAND_RULES_DISABLED_MSG);
            return;
        }

        context.faction.clearRules();
        context.msg(TL.COMMAND_RULES_CLEAR_SUCCESS);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RULES_DESCRIPTION;
    }
}