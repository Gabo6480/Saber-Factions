package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.CommandVisibility;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.util.TextUtil;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;


public abstract class FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */

    public SimpleDateFormat sdf = new SimpleDateFormat(TL.DATE_FORMAT.toString());

    // Command Aliases
    public List<String> aliases;

    // Information on the args
    public LinkedHashMap<String, Function<CommandContext, List<String>>> requiredArgs;
    public LinkedHashMap<String, String> optionalArgs;

    // Requirements to execute this command
    public CommandRequirements requirements;
    /*
        Subcommands
     */
    public List<FCommand> subCommands;
    /*
        Help
     */
    public List<String> helpLong;
    public CommandVisibility visibility;
    private String helpShort;

    public FCommand() {

        requirements = new CommandRequirements.Builder(null).build();

        this.subCommands = new ArrayList<>();
        this.aliases = new ArrayList<>();

        this.requiredArgs = new LinkedHashMap<>();
        this.optionalArgs = new LinkedHashMap<>();

        this.helpShort = null;
        this.helpLong = new ArrayList<>();
        this.visibility = CommandVisibility.VISIBLE;
    }

    public abstract void perform(CommandContext context);

    public void execute(CommandContext context) {
        // Is there a matching sub command?
        if (context.args.size() > 0) {
            String firstArg = context.args.get(0).toLowerCase();
            for (FCommand subCommand : this.subCommands) {
                if (subCommand.aliases.contains(firstArg)) {
                    context.args.remove(0);
                    context.commandChain.add(this);
                    subCommand.execute(context);
                    return;
                }
            }
        }

        if (!isValidCall(context)) {
            return;
        }

        if (!this.isEnabled(context)) {
            return;
        }

        perform(context);
    }

    public List<String> complete(CommandContext context){
        List<String> completions = null;
        int argCount = context.args.size();
        if (argCount > 0) {
            completions = new ArrayList<>();
            String firstArg = context.args.get(0).toLowerCase();


            if(firstArg.isEmpty()){
                for (FCommand subCommand : this.subCommands) {
                    if (subCommand.isVisible(context))
                        completions.addAll(subCommand.aliases);
                }
            }
            else if(argCount == 1) {
                // Is there a matching sub command?
                for (FCommand subCommand : this.subCommands) {
                    // We check each alias to see if they match whatever is currently written
                    for (String s : subCommand.aliases) {
                        if (s.startsWith(firstArg)) {
                            completions.addAll(subCommand.aliases);
                            break;
                        }
                    }
                }
            }
            else {
                // If there is at least 2 arguments, then check if there is an exactly matching subcommand and drill down into it
                for (FCommand subCommand : this.subCommands) {
                    if (subCommand.aliases.contains(firstArg)) {
                        context.args.remove(0); // Remove this argument from the context

                        return subCommand.complete(context);
                    }
                }
            }

            int currentArgIndex = argCount - 1;
            // If there are no exactly matching subcommands, then we start processing the requiredArgs
            if(argCount <= requiredArgs.size()){
                if(context.argAsString(currentArgIndex).isEmpty()){
                    String reqArgName = new ArrayList<>(requiredArgs.keySet()).get(currentArgIndex);

                    if(reqArgName != null)
                        completions.add("<" + reqArgName + ">");
                }

                List<String> result = new ArrayList<>(requiredArgs.values()).get(currentArgIndex).apply(context);
                if(result != null) completions.addAll(result.stream().map(ChatColor::stripColor).collect(Collectors.toList()));
            }
        }
        if(completions == null || completions.isEmpty()) return null;

        return completions;
    }

    public boolean isValidCall(CommandContext context) {
        return requirements.computeRequirements(context, true) && areArgsValid(context);
    }

    public boolean isEnabled(CommandContext context) {
        if (FactionsPlugin.getInstance().getLocked() && requirements.disableOnLock) {
            context.msg("<b>Factions was locked by an admin. Please try again later.");
            return false;
        }
        return true;
    }

    public boolean isVisible(CommandContext context){
        return (!this.requirements.playerOnly || context.player != null) && (this.requirements.permission == null || context.sender.hasPermission(this.requirements.permission.node))
                && this.visibility != CommandVisibility.INVISIBLE;
    }

    public boolean areArgsValid(CommandContext context) {
        if (context.args.size() < this.requiredArgs.size()) {
            if (context.sender != null) {
                context.msg(TL.GENERIC_ARGS_TOOFEW);
                context.sender.sendMessage(this.getUsageTemplate(context));
            }
            return false;
        }

        if (context.args.size() > this.requiredArgs.size() + this.optionalArgs.size() && this.requirements.errorOnManyArgs) {
            if (context.sender != null) {
                // Get the to many string slice
                List<String> theToMany = context.args.subList(this.requiredArgs.size() + this.optionalArgs.size(), context.args.size());
                context.msg(TL.GENERIC_ARGS_TOOMANY, TextUtil.implode(theToMany, " "));
                context.sender.sendMessage(this.getUsageTemplate(context));
            }
            return false;
        }
        return true;
    }

    public void addSubCommand(FCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    public String getHelpShort() {
        if (this.helpShort == null) {
            return getUsageTranslation().toString();
        }

        return this.helpShort;
    }

    public void setHelpShort(String val) {
        this.helpShort = val;
    }

    public abstract TL getUsageTranslation();


    /*
        Common Logic
     */
    public static List<String> getToolTips(FPlayer player) {
        List<String> lines = new ArrayList<>();
        for (String s : FactionsPlugin.getInstance().getConfig().getStringList("tooltips.show")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', replaceFPlayerTags(s, player)));
        }
        return lines;
    }

    public static List<String> getToolTips(Faction faction) {
        List<String> lines = new ArrayList<>();
        for (String s : FactionsPlugin.getInstance().getConfig().getStringList("tooltips.list")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', replaceFactionTags(s, faction)));
        }
        return lines;
    }

    public static String replaceFPlayerTags(String s, FPlayer player) {
        if (s.contains("{balance}")) {
            String balance = Econ.isSetup() ? Econ.getFriendlyBalance(player) : "no balance";
            s = s.replace("{balance}", balance);
        }
        if (s.contains("{lastSeen}")) {
            String humanized = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - player.getLastLoginTime(), true, true) + " ago";
            String lastSeen = player.isOnline() ? ChatColor.GREEN + "Online" : (System.currentTimeMillis() - player.getLastLoginTime() < 432000000 ? ChatColor.YELLOW + humanized : ChatColor.RED + humanized);
            s = s.replace("{lastSeen}", lastSeen);
        }
        if (s.contains("{power}")) {
            String power = player.getPowerRounded() + "/" + player.getPowerMaxRounded();
            s = s.replace("{power}", power);
        }
        if (s.contains("{group}")) {
            String group = FactionsPlugin.getInstance().getPrimaryGroup(Bukkit.getOfflinePlayer(UUID.fromString(player.getId())));
            s = s.replace("{group}", group);
        }
        return s;
    }

    public static String replaceFactionTags(String s, Faction faction) {
        if (s.contains("{power}")) {
            s = s.replace("{power}", String.valueOf(faction.getPowerRounded()));
        }
        if (s.contains("{maxPower}")) {
            s = s.replace("{maxPower}", String.valueOf(faction.getPowerMaxRounded()));
        }
        if (s.contains("{leader}")) {
            FPlayer fLeader = faction.getFPlayerAdmin();
            String leader = fLeader == null ? "Server" : fLeader.getName().substring(0, fLeader.getName().length() > 14 ? 13 : fLeader.getName().length());
            s = s.replace("{leader}", leader);
        }
        if (s.contains("{chunks}")) {
            s = s.replace("{chunks}", String.valueOf(faction.getLandRounded()));
        }
        if (s.contains("{members}")) {
            s = s.replace("{members}", String.valueOf(faction.getSize()));

        }
        if (s.contains("{online}")) {
            s = s.replace("{online}", String.valueOf(faction.getOnlinePlayers().size()));
        }
        return s;
    }

    /*
    Help and Usage information
    */
    public String getUsageTemplate(CommandContext context, boolean addShortHelp) {
        StringBuilder ret = new StringBuilder();
        ret.append(CC.translate(TL.COMMAND_USEAGE_TEMPLATE_COLOR.toString()));
        ret.append('/');

        for (FCommand fc : context.commandChain) {
            ret.append(TextUtil.implode(fc.aliases, ","));
            ret.append(' ');
        }

        ret.append(TextUtil.implode(this.aliases, ","));

        List<String> args = new ArrayList<>();

        for (String requiredArg : this.requiredArgs.keySet()) {
            args.add("<" + requiredArg + ">");
        }

        for (Map.Entry<String, String> optionalArg : this.optionalArgs.entrySet()) {
            String val = optionalArg.getValue();
            if (val == null) {
                val = "";
            } else {
                val = "=" + val;
            }
            args.add("[" + optionalArg.getKey() + val + "]");
        }

        if (args.size() > 0) {
            ret.append(FactionsPlugin.getInstance().txt.parseTags(" "));
            ret.append(TextUtil.implode(args, " "));
        }

        if (addShortHelp) {
            ret.append(FactionsPlugin.getInstance().txt.parseTags(" "));
            ret.append(this.getHelpShort());
        }

        return ret.toString();
    }

    public String getUsageTemplate(CommandContext context) {
        return getUsageTemplate(context, false);
    }

}
