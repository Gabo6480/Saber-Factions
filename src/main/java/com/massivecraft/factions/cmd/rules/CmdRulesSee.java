package com.massivecraft.factions.cmd.rules;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;

import java.util.LinkedList;
import java.util.List;

public class CmdRulesSee extends FCommand {

    /**
     * @author Illyria Team
     *
     * This command is used to see all rules from the sender's current facction's custom rules text, it basically does the same as /f rules, it was added for clarity
     */

    public CmdRulesSee() {
        super();
        aliases.addAll(Aliases.rules_see);

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

        LinkedList<String> rules = context.faction.getRules();
        if (rules.size() == 0) {
            List<String> ruleList = FactionsPlugin.getInstance().getConfig().getStringList("frules.default-rules");
            context.sendMessage(CC.translate(ruleList));

        } else {
            context.sendMessage(CC.translate(rules));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RULES_DESCRIPTION;
    }
}