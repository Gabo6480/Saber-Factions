package com.massivecraft.factions.cmd.tnt;

import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.audit.FLogType;
import com.massivecraft.factions.cmd.core.Aliases;
import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.CommandRequirements;
import com.massivecraft.factions.cmd.core.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CmdTntAddAll extends FCommand {

    /**
     * @author Illyria Team
     *
     * This command is used take all tnt from the sender's current faction's TNT Bank
     */

    public CmdTntAddAll() {
        super();
        this.aliases.addAll(Aliases.tnt_addall);

        this.requirements = new CommandRequirements.Builder(Permission.TNT)
                .playerOnly()
                .memberOnly()
                .withAction(PermissableAction.TNTBANK)
                .build();
    }


    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.instance.getConfig().getBoolean("ftnt.Enabled")) {
            context.msg(TL.COMMAND_TNT_DISABLED_MSG);
            return;
        }

        Inventory inv = context.player.getInventory();
        int invTnt = 0;
        for (int i = 0; i <= inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                continue;
            }
            if (inv.getItem(i).getType() == Material.TNT) {
                invTnt += inv.getItem(i).getAmount();
            }
        }
        if (invTnt <= 0) {
            context.msg(TL.COMMAND_TNT_DEPOSIT_NOTENOUGH);
            return;
        }
        if (context.faction.getTnt() + invTnt > context.faction.getTntBankLimit()) {
            context.msg(TL.COMMAND_TNT_EXCEEDLIMIT);
            return;
        }
        CmdTnt.removeItems(context.player.getInventory(), new ItemStack(Material.TNT), invTnt);
        context.player.updateInventory();
        context.faction.addTnt(invTnt);
        context.msg(TL.COMMAND_TNT_DEPOSIT_SUCCESS);
        FactionsPlugin.instance.getFlogManager().log(context.faction, FLogType.F_TNT, context.fPlayer.getName(), "DEPOSITED", invTnt + "x TNT");

        context.fPlayer.sendMessage(CC.translate(TL.COMMAND_TNT_AMOUNT.toString().replace("{amount}", context.faction.getTnt() + "").replace("{maxAmount}", context.faction.getTntBankLimit() + "")));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TNT_DESCRIPTION;
    }
}
