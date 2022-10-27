package com.massivecraft.factions.cmd.core.args;

import com.massivecraft.factions.cmd.core.CommandContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OnOffArgumentProvider extends ListStringArgumentProvider{

    public OnOffArgumentProvider(String defaultValue) {
        super("on/off", defaultValue, "on", "off");
    }

    public OnOffArgumentProvider() {
        super("", "toggle", "on", "off");
    }
}
