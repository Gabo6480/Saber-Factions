package com.massivecraft.factions.cmd.rules;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.SingleWordArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdRulesRemove extends FCommand {

    /**
     * @author Illyria Team
     *
     * This command is used to remove a specified rule from the sender's current facction's custom rules text
     */

    public CmdRulesRemove() {
        super();
        aliases.addAll(Aliases.rules_remove);

        this.requiredArgs.add(new IntegerArgumentProvider("row", (integer, context) -> integer > 0 && integer < context.faction.getRules().size()));

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

        if(context.args.size() <= 0){
            context.msg(TL.COMMAND_RULES_REMOVE_INVALIDARGS);
            return;
        }

        int ruleIndex = context.argAsInt(0) - 1;

        context.faction.removeRule(ruleIndex);
        context.msg(TL.COMMAND_RULES_REMOVE_SUCCESS);;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RULES_DESCRIPTION;
    }
}