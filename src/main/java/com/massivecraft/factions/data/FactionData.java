package com.massivecraft.factions.data;

import com.google.common.collect.Maps;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.helpers.FactionDataHelper;
import com.massivecraft.factions.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: Driftay
 * @Date: 2/11/2022 4:37 PM
 */
public class FactionData {

    private String factionID;
    private String factionTag;
    private Map<String, Object> map;
    private boolean saving;

    public FactionData(Faction faction) {
        this.factionTag = faction.getTag();
        this.factionID = faction.getId();
        this.map = Maps.newHashMap();
        this.saving = false;
    }

    public void addStoredValue(String key, Object value) {
        this.map.put(key, value);
    }

    public boolean isSaving() {
        return this.saving;
    }

    public YamlConfiguration getConfiguration() {
        return YamlConfiguration
                .loadConfiguration(new File(FactionsPlugin.getInstance().getDataFolder()
                        + "/faction-data/" + factionID + ".yml"));
    }

    public Object getStoredValue(String key) {
        return this.map.get(key);
    }

    public boolean hasStoredValue(String key) {
        return this.map.containsKey(key);
    }

    public boolean hasConfigValue(String key) {
        return this.getConfiguration().contains(key);
    }

    public boolean hasValue(String key) {
        boolean value = hasConfigValue(key) || hasStoredValue(key);
        if(!value) {
            setDefaultPath(key, null);
        }
        return value;
    }

    public void setDefaultPath(String path, Object value) {
        this.map.put(path, value);
    }

    public Map<String, Object> getStoredValues() {
        return this.map;
    }

    public void setStoredValues(Map<String, Object> map) {
        this.map = map;
    }

    public Object getValue(String key) {

        if (this.map.containsKey(key)) {
            return this.map.get(key);
        }

        Object value = getConfiguration().get(key);
        this.map.put(key, value);
        return value;
    }


    public void deleteFactionData(Faction faction) {
        File file = new File(FactionsPlugin.getInstance().getDataFolder() + "/faction-data/"
                + this.factionID + ".yml");

        if(file.delete()) {
            Logger.print("Deleting faction-data for faction " + faction.getTag(), Logger.PrefixType.DEFAULT);
        } else {
            Logger.print("Failed to delete faction-data for faction " + faction.getTag(), Logger.PrefixType.WARNING);
        }
    }

    public void save() {
        if(this.isSaving()) {
            return;
        }
        this.saving = true;
        File file = new File(FactionsPlugin.getInstance().getDataFolder() + "/faction-data/"
                + this.factionID + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        Bukkit.getLogger().info("[FactionData] Saving " + this.factionTag + "'s Data to the disk");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            configuration.set(key, value);
        }

        try {
            configuration.save(file);
            this.saving = false;
        } catch (IOException e) {
            e.printStackTrace();
            this.saving = false;
        }
    }

    @Deprecated
    public void remove() {
        FactionDataHelper.getData().remove(this);
    }

    public String getFactionID() {
        return factionID;
    }

    public String getFactionTag() {
        return factionTag;
    }

    public void removeSafely() {
        this.save();
        this.remove();
    }

}
