package com.massivecraft.factions.cmd.rules;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.ListStringArgumentProvider;
import com.massivecraft.factions.cmd.econ.CmdMoneyBalance;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CmdRules extends FCommand {

    /**
     * @author Illyria Team, Modified by Gabo6480
     *
     * This command is used to get the sender's current facction's custom rules text, also, is the base command for all rule related actions
     */

    public CmdRulesAdd cmdRulesAdd = new CmdRulesAdd();
    public CmdRulesSet cmdRulesSet = new CmdRulesSet();
    public CmdRulesRemove cmdRulesRemove = new CmdRulesRemove();
    public CmdRulesClear cmdRulesClear = new CmdRulesClear();
    public CmdRulesSee cmdRulesSee = new CmdRulesSee();

    public CmdRules() {
        super();
        aliases.addAll(Aliases.rules);

        this.requirements = new CommandRequirements.Builder(Permission.RULES)
                .playerOnly()
                .memberOnly()
                .build();

        this.addSubCommand(cmdRulesAdd);
        this.addSubCommand(cmdRulesSet);
        this.addSubCommand(cmdRulesRemove);
        this.addSubCommand(cmdRulesClear);
        this.addSubCommand(cmdRulesSee);
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