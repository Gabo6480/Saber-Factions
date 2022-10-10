package com.massivecraft.factions.missions;

public class Mission {

    /**
     * @author Driftay
     */

    private long progress;
    private String name;
    private MissionType type;

    public Mission(String name, MissionType type) {
        this.name = name;
        this.type = type;
    }

    public long getProgress() {
        return progress;
    }

    public void incrementProgress() {
        ++progress;
    }

    public String getName() {
        return name;
    }

    public MissionType getType() {
        return type;
    }
}
