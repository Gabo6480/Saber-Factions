package com.massivecraft.factions.struct;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.util.LazyLocation;
import org.bukkit.Location;

import java.io.Serializable;

public class Warp implements Serializable {
    String password;
    LazyLocation location;

    transient Faction faction;
    transient String name;

    public Warp(String name, Location location, Faction faction){
        this.name = name;
        this.faction = faction;
        this.location = new LazyLocation(location);
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public boolean hasPassword(){
        return password != null;
    }
    public boolean isPassword(String password){
        return hasPassword() && this.password.equals(password);
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Faction getFaction() {
        return faction;
    }
    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
