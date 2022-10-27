package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.cmd.core.args.ListStringArgumentProvider;
import com.massivecraft.factions.cmd.core.args.number.IntegerArgumentProvider;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.function.Function;

public class CmdTop extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens - Further modified by Gabo6480
     *
     * This command is used to get a decending ordered list of all faction depending on the selected criteria
     */

    public CmdTop() {
        super();
        this.aliases.addAll(Aliases.top);
        this.requiredArgs.add(new ListStringArgumentProvider("criteria", null,
                "members",
                "players",
                "start",
                "age",
                "power",
                "maxpower",
                "points",
                "kills",
                "deaths",
                "alts",
                "land",
                "claims",
                "online",
                "money",
                "balance",
                "bal",
                "allies",
                "enemies",
                "truces",
                "neutrals"));
        this.optionalArgs.add(new IntegerArgumentProvider("page", 1, (integer, context) -> integer > 0));

        this.requirements = new CommandRequirements.Builder(Permission.TOP)
                .build();
    }


    @Override
    public void perform(CommandContext context) {
        // Get all Factions and remove non player ones.
        ArrayList<Faction> factionList = Factions.getInstance().getAllFactions();
        factionList.remove(Factions.getInstance().getWilderness());
        factionList.remove(Factions.getInstance().getSafeZone());
        factionList.remove(Factions.getInstance().getWarZone());

        String criteria = context.argAsString(0).toLowerCase();

        Function<Faction, Number> getValue;
        ValueType valueType;

        // TODO: Better way to sort?
        switch (criteria){
            case "members":
            case "players":
            {
                getValue = f -> f.getFPlayers().size();
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "start":
            case "age":
            {
                getValue = Faction::getFoundedDate;
                valueType = ValueType.TIMESTAMP;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "power":
            {
                getValue = Faction::getPower;
                valueType = ValueType.DOUBLE;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "maxpower":
            {
                getValue = Faction::getPowerMax;
                valueType = ValueType.DOUBLE;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "points":
            {
                getValue = Faction::getPoints;
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "kills":
            {
                getValue = Faction::getKills;
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "deaths":
            {
                getValue = Faction::getDeaths;
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "alts":
            {
                getValue = f -> f.getAltPlayers().size();
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "land":
            case "claims":
            {
                getValue = Faction::getLandRounded;
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "online":
            {
                getValue = f -> f.getFPlayersWhereOnline(true).size();
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "money":
            case "balance":
            case "bal":
            {
                getValue = f -> {
                    double bal = f.getFactionBalance();
                    // Lets get the balance of /all/ the players in the Faction.
                    for (FPlayer fp : f.getFPlayers()) {
                        bal += Econ.getBalance(fp.getAccountId());
                    }
                    return bal;
                };
                valueType = ValueType.MONEY;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "allies":
            {
                getValue = f -> f.getRelationCount(Relation.ALLY);
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "enemies":
            {
                getValue = f -> f.getRelationCount(Relation.ENEMY);
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "truces":
            {
                getValue = f -> f.getRelationCount(Relation.TRUCE);
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            case "neutrals":
            {
                getValue = f -> f.getRelationCount(Relation.NEUTRAL);
                valueType = ValueType.INTEGER;
                factionList.sort((f1, f2) -> sortFaction(f1, f2, getValue));
            }
            break;
            default:
            {
                context.msg(TL.COMMAND_TOP_INVALID, criteria);
                return;
            }
        }

        ArrayList<String> lines = new ArrayList<>();

        final int pageheight = 9;
        int pagenumber = context.argAsInt(1, 1);
        int pagecount = (factionList.size() / pageheight) + 1;
        if (pagenumber > pagecount) {
            pagenumber = pagecount;
        } else if (pagenumber < 1) {
            pagenumber = 1;
        }
        int start = (pagenumber - 1) * pageheight;
        int end = start + pageheight;
        if (end > factionList.size()) {
            end = factionList.size();
        }

        lines.add(TL.COMMAND_TOP_TOP.format(criteria.toUpperCase(), pagenumber, pagecount));

        int rank = 1;
        for (Faction faction : factionList.subList(start, end)) {
            // Get the relation color if player is executing this.
            String fac = context.sender instanceof Player ? faction.getRelationTo(context.fPlayer).getColor() + faction.getTag() : faction.getTag();
            lines.add(TL.COMMAND_TOP_LINE.format(rank, fac, getValueString(faction, valueType, getValue)));
            rank++;
        }

        context.sendMessage(lines);
    }

    private <N extends Number> int sortFaction(Faction f1, Faction f2, Function<Faction, N> getValue){
        double f1Size = getValue.apply(f1).doubleValue();
        double f2Size = getValue.apply(f2).doubleValue();
        if (f1Size < f2Size) {
            return 1;
        } else if (f1Size > f2Size) {
            return -1;
        }
        return 0;
    }

    private String getValueString(Faction faction, ValueType valueType, Function<Faction, Number> getValue) {
        switch (valueType){
            case MONEY: return  "$" + String.format("%.2f",getValue.apply(faction).doubleValue());
            case INTEGER: return String.valueOf(getValue.apply(faction));
            case DOUBLE: return String.format("%.2f",getValue.apply(faction).doubleValue());
            case TIMESTAMP: return TL.sdf.format(getValue.apply(faction).longValue());
        }
        return String.valueOf(getValue.apply(faction));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TOP_DESCRIPTION;
    }

    private enum ValueType {
        INTEGER,
        TIMESTAMP,
        DOUBLE,
        MONEY;
    }
}
