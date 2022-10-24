package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import java.util.stream.Collectors;

public class CmdViewChest extends FCommand {

    /**
     * @author Driftay
     *
     * This command is used to view specified faction's faction chest
     */

    public CmdViewChest() {
        super();
        this.aliases.addAll(Aliases.viewChest);

        this.requiredArgs.put("faction", context -> Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList()));

        this.requirements = new CommandRequirements.Builder(Permission.VIEWCHEST)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.getInstance().getConfig().getBoolean("fchest.Enabled")) {
            context.msg(TL.GENERIC_DISABLED, "Faction Chests");
            return;
        }

        Faction myFaction = context.fPlayer.getFaction();

        Faction faction = context.argAsFaction(0, context.fPlayer == null ? null : myFaction);
        if (faction == null) {
            return;
        }
        context.player.openInventory(faction.getChestInventory());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_VIEWCHEST_DESCRIPTION;
    }
}

