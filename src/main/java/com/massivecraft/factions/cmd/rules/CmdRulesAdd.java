package com.massivecraft.factions.cmd.rules;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.ListStringArgumentProvider;
import com.massivecraft.factions.cmd.core.args.SingleWordArgumentProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;

import java.util.HashMap;
import java.util.List;

public class CmdRulesAdd extends FCommand {

    /**
     * @author Illyria Team
     *
     * This command is used to add a rule the sender's current facction's custom rules text
     */

    public CmdRulesAdd() {
        super();
        aliases.addAll(Aliases.rules_add);

        // TODO: Create an ArgumentProvider that works for strings with spaces
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

        if(context.args.size() == 0){
            context.msg(TL.COMMAND_RULES_ADD_INVALIDARGS);
            return;
        }

        StringBuilder string = new StringBuilder("");
        for (int i = 0; i <= context.args.size(); i++) {
            string.append(" ").append(context.args.get(i));
        }
        context.faction.addRule(string.toString());
        context.msg(TL.COMMAND_RULES_ADD_SUCCESS);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RULES_DESCRIPTION;
    }
}