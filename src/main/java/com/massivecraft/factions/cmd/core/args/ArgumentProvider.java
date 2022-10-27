package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArgumentProvider {
    @NotNull
    String getName();

    List<String> getCompletions(CommandContext context, int currentArgumentIndex);
}
