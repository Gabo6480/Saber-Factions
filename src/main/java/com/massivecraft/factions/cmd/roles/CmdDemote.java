package com.massivecraft.factions.cmd.roles;

import com.massivecraft.factions.cmd.core.Aliases;

public class CmdDemote extends FPromoteCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */

    public CmdDemote() {
        aliases.addAll(Aliases.roles_demote);
        this.relative = -1;
    }
}
