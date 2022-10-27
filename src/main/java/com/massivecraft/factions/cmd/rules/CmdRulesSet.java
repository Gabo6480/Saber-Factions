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

public class CmdRulesSet extends FCommand {

    /**
     * @author Illyria Team
     *
     * This subcommand is used to modify a specific rule index in the sender's current facction's custom rules text
     */

    public CmdRulesSet() {
        super();
        aliases.addAll(Aliases.rules_set);

        // TODO: Create an ArgumentProvider that works for strings with spaces
        this.requiredArgs.add(new IntegerArgumentProvider("row", (integer, context) -> integer > 0 && integer < context.faction.getRules().size()));
        this.requiredArgs.add(new SingleWordArgumentProvider("rule"));

        this.requirements = new CommandRequirements.Builder(Permission.RULES)
                .playerOnly()
                .memberOnly()
                .noErrorOnManyArgs()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.getInstance().getConfig().getBoolean("frules.Enabled")) {
            context.msg(TL.COMMAND_RULES_DISABLED_MSG);
            return;
        }

        if(context.args.size() <= 1){
            context.msg(TL.COMMAND_RULES_SET_INVALIDARGS);
            return;
        }

        int ruleIndex = context.argAsInt(1) - 1;

        StringBuilder string = new StringBuilder();
        for (int i = 1; i <= context.args.size() - 1; i++) {
            string.append(" ").append(context.args.get(i));
        }
        context.faction.setRule(ruleIndex, string.toString());
        context.msg(TL.COMMAND_RULES_SET_SUCCESS);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RULES_DESCRIPTION;
    }
}