package com.massivecraft.factions.missions;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MissionHandler implements Listener {

    /**
     * @author Driftay
     */

    public static final String matchAnythingRegex = ".*";

    private static FactionsPlugin plugin;

    public MissionHandler(FactionsPlugin plugin) {
        MissionHandler.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerBreed(EntityBreedEvent e) {
        if (!(e.getBreeder() instanceof Player)) {
            return;
        }
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer((Player) e.getBreeder());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.BREED, (mission, section) -> {
            String entity = section.getString("Mission.Entity", matchAnythingRegex);
            return e.getEntityType().toString().matches(entity) ? 1 : -1;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTame(EntityTameEvent e) {
        if (!(e.getOwner() instanceof Player)) {
            return;
        }
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer((Player) e.getOwner());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.TAME, (mission, section) -> {
            String entity = section.getString("Mission.Entity", matchAnythingRegex);
            return e.getEntityType().toString().matches(entity) ? 1 : -1;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() == null || e.getEntity().getKiller() == null) {
            return;
        }
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getEntity().getKiller());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.KILL, (mission, section) -> {
            String entity = section.getString("Mission.Entity", matchAnythingRegex);
            return e.getEntityType().toString().matches(entity) ? 1 : -1;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.MINE, (mission, section) -> {
            String item = section.getString("Mission.Item", matchAnythingRegex);
            return e.getBlock().getType().toString().matches(item) ? 1 : -1;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.PLACE, (mission, section) -> {
            String item = section.getString("Mission.Item", matchAnythingRegex);
            return e.getBlockPlaced().getType().toString().matches(item) ? 1 : -1;
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.FISH, (mission, section) -> {
            String item = section.getString("Mission.Item", matchAnythingRegex);
            if(e.getCaught() instanceof Item) {
                Item caughtItem = (Item) e.getCaught();
                return caughtItem.getItemStack().getType().toString().matches(item) ? 1 : -1;
            }
            return -1;
        });
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent e) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getEnchanter());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.ENCHANT, (mission, section) -> {
            String item = section.getString("Mission.Item", matchAnythingRegex);
            return e.getItem().getType().toString().matches(item) ? 1 : -1;
        });
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
        if (fPlayer == null) {
            return;
        }

        handleMissionsOfType(fPlayer, MissionType.CONSUME, (mission, section) -> {
            String item = section.getString("Mission.Item", matchAnythingRegex);
            return e.getItem().getType().toString().matches(item) ? 1 : -1;
        });
    }

    public static void handleMissionsOfType(FPlayer fPlayer, MissionType missionType, BiFunction<Mission, ConfigurationSection, Integer> missionConsumer){
        getMissionsOfType(fPlayer, missionType).forEach(mission -> {
                    ConfigurationSection section = plugin.getFileManager().getMissions().getConfig().getConfigurationSection("Missions." + mission.getName());
                    int missionResult = missionConsumer.apply(mission, section);
                    if(missionResult > 0){
                        mission.incrementProgress(missionResult);
                        checkIfDone(fPlayer, mission, section);
                    }
                });
    }

    public static Stream<Mission> getMissionsOfType(FPlayer fPlayer, MissionType missionType){
        return fPlayer.getFaction().getMissions().values().stream()
                .filter(mission -> mission.getType() == missionType);
    }

    private static void checkIfDone(FPlayer fPlayer, Mission mission, @Nullable ConfigurationSection section) {
        if(section == null)
            return;

        if (mission.getProgress() < section.getLong("Mission.Amount")) {
            return;
        }
        for (String command : section.getStringList("Reward.Commands")) {
            FactionsPlugin.getInstance().getServer().dispatchCommand(FactionsPlugin.getInstance().getServer().getConsoleSender(), command.replace("%faction%", fPlayer.getFaction().getTag()).replace("%player%", fPlayer.getPlayer().getName()));
        }
        fPlayer.getFaction().getMissions().remove(mission.getName());
        fPlayer.getFaction().msg(TL.MISSION_MISSION_FINISHED, CC.translate(section.getString("Name")));
        fPlayer.getFaction().getCompletedMissions().add(mission.getName());
    }
}
