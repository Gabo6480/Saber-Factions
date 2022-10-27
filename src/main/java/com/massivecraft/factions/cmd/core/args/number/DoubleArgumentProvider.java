package com.massivecraft.factions.cmd.core.args.number;

import com.massivecraft.factions.cmd.core.CommandContext;
import com.massivecraft.factions.cmd.core.args.number.NumberArgumentProvider;

import java.util.function.BiFunction;

public class DoubleArgumentProvider extends NumberArgumentProvider<Double> {
    public DoubleArgumentProvider(String name, Double defaultValue, BiFunction<Double, CommandContext, Boolean> numberFilter) {
        super(name, defaultValue, Double::parseDouble, numberFilter);
    }

    public DoubleArgumentProvider(String name, BiFunction<Double, CommandContext, Boolean> numberFilter) {
        super(name, null, Double::parseDouble, numberFilter);
    }

    public DoubleArgumentProvider(String name) {
        super(name, null, Double::parseDouble, (num, ctx) -> true);
    }
}
