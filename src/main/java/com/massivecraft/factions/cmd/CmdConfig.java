package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class CmdConfig extends FCommand {

    /**
     * This command is used to change the settings inside of conf.yml while the plugin is still running.
     */

    private static final HashMap<String, String> properFieldNames = new HashMap<>();

    public CmdConfig() {
        super();
        this.aliases.addAll(Aliases.config);

        this.requiredArgs.put("setting",
                (context) -> {
                    List<String> completions = new ArrayList<>();
                    for (Field field : Conf.class.getDeclaredFields()) completions.add(field.getName());
                    return  completions;
                });

        this.requiredArgs.put("value",
                (context) -> {
                    List<String> completions = new ArrayList<>();

                    String fieldName = getAsValidFieldName(context.argAsString(0));
                    String value = concatArguments(context.args.subList(1,context.args.size()));

                    try {
                        Field target = Conf.class.getField(fieldName);

                        // boolean
                        if (target.getType() == boolean.class) {
                            completions.add("true");
                            completions.add("false");
                        }

                        // int
                        else if (target.getType() == int.class) {
                            // Just to check if it can be parsed into an int
                            Integer.parseInt(value);

                            for (int i = 0; i < 10; i++) {
                                String completeInt = value + i;
                                // Just to check if it can be parsed into an int
                                Integer.parseInt(completeInt);
                                completions.add(completeInt);
                            }
                        }

                        // long
                        else if (target.getType() == long.class) {
                            // Just to check if it can be parsed into a long
                            Long.parseLong(value);

                            for (int i = 0; i < 10; i++) {
                                String completeInt = value + i;
                                // Just to check if it can be parsed into a long
                                Long.parseLong(completeInt);
                                completions.add(completeInt);
                            }
                        }

                        // double
                        else if (target.getType() == double.class) {
                            // Just to check if it can be parsed into a double
                            Double.parseDouble(value);
                            if(!value.contains(".")) completions.add(value + ".");
                            for (int i = 0; i < 10; i++) {
                                String completeInt = value + i;
                                // Just to check if it can be parsed into a double
                                Double.parseDouble(completeInt);
                                completions.add(completeInt);
                            }
                        }

                        // float
                        else if (target.getType() == float.class) {
                            // Just to check if it can be parsed into a float
                            Float.parseFloat(value);
                            completions.add(value + ".");
                            for (int i = 0; i < 10; i++) {
                                String completeInt = value + i;
                                // Just to check if it can be parsed into a float
                                Float.parseFloat(completeInt);
                                completions.add(completeInt);
                            }

                        }

                        // String
                        else if (target.getType() == String.class) {
                            target.set(null, value);
                            completions.add("[<string>]");
                        }

                        // ChatColor
                        else if (target.getType() == ChatColor.class) {
                            completions.addAll(Arrays.stream(ChatColor.values()).map(ChatColor::toString).collect(Collectors.toList()));
                        }

                        // Set<?> or other parameterized collection
                        else if (target.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType targSet = (ParameterizedType) target.getGenericType();
                            Type innerType = targSet.getActualTypeArguments()[0];

                            // not a Set, somehow, and that should be the only collection we're using in Conf.java
                            if (targSet.getRawType() != Set.class) {
                                return null;
                            }

                            // Set<Material>
                            else if (innerType == Material.class) {
                                completions.addAll(Arrays.stream(Material.values()).map(Material::toString).collect(Collectors.toList()));
                            }

                            // Set<String>
                            else if (innerType == String.class) {
                                @SuppressWarnings("unchecked") Set<String> stringSet = (Set<String>) target.get(null);

                                completions.add("[<new string>]");
                                completions.addAll(stringSet);
                            }

                            // Set of unknown type
                            else {
                                return null;
                            }
                        }

                        // unknown type
                        else {
                            return null;
                        }
                    }
                    catch (NumberFormatException ex) {
                        if(completions.isEmpty())
                            return null;
                    }
                    catch (Exception ignored) {
                        return null;
                    }

                    return  completions;
                });

        this.requirements = new CommandRequirements.Builder(Permission.CONFIG)
                .noErrorOnManyArgs()
                .build();
    }

    @Override
    public void perform(CommandContext context) {

        String fieldName = getAsValidFieldName(context.argAsString(0));

        if (fieldName == null || fieldName.isEmpty()) {
            context.msg(TL.COMMAND_CONFIG_NOEXIST, context.argAsString(0));
            return;
        }

        String success;
        // Concat all subsequent args after the 1st one
        String value = concatArguments(context.args.subList(1,context.args.size()));

        try {
            Field target = Conf.class.getField(fieldName);

            // boolean
            if (target.getType() == boolean.class) {
                boolean targetValue = context.strAsBool(value);
                target.setBoolean(null, targetValue);

                if (targetValue) {
                    success = "\"" + fieldName + TL.COMMAND_CONFIG_SET_TRUE;
                } else {
                    success = "\"" + fieldName + TL.COMMAND_CONFIG_SET_FALSE;
                }
            }

            // int
            else if (target.getType() == int.class) {
                try {
                    int intVal = Integer.parseInt(value);
                    target.setInt(null, intVal);
                    success = "\"" + fieldName + TL.COMMAND_CONFIG_OPTIONSET + intVal + ".";
                } catch (NumberFormatException ex) {
                    context.sendMessage(TL.COMMAND_CONFIG_INTREQUIRED.format(fieldName));
                    return;
                }
            }

            // long
            else if (target.getType() == long.class) {
                try {
                    long longVal = Long.parseLong(value);
                    target.setLong(null, longVal);
                    success = "\"" + fieldName + TL.COMMAND_CONFIG_OPTIONSET + longVal + ".";
                } catch (NumberFormatException ex) {
                    context.sendMessage(TL.COMMAND_CONFIG_LONGREQUIRED.format(fieldName));
                    return;
                }
            }

            // double
            else if (target.getType() == double.class) {
                try {
                    double doubleVal = Double.parseDouble(value);
                    target.setDouble(null, doubleVal);
                    success = "\"" + fieldName + TL.COMMAND_CONFIG_OPTIONSET + doubleVal + ".";
                } catch (NumberFormatException ex) {
                    context.sendMessage(TL.COMMAND_CONFIG_DOUBLEREQUIRED.format(fieldName));
                    return;
                }
            }

            // float
            else if (target.getType() == float.class) {
                try {
                    float floatVal = Float.parseFloat(value);
                    target.setFloat(null, floatVal);
                    success = "\"" + fieldName + TL.COMMAND_CONFIG_OPTIONSET + floatVal + ".";
                } catch (NumberFormatException ex) {
                    context.sendMessage(TL.COMMAND_CONFIG_FLOATREQUIRED.format(fieldName));
                    return;
                }
            }

            // String
            else if (target.getType() == String.class) {
                target.set(null, value);
                success = "\"" + fieldName + TL.COMMAND_CONFIG_OPTIONSET + value + "\".";
            }

            // ChatColor
            else if (target.getType() == ChatColor.class) {
                ChatColor newColor = null;
                try {
                    newColor = ChatColor.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException ex) {

                }
                if (newColor == null) {
                    context.sendMessage(TL.COMMAND_CONFIG_INVALID_COLOUR.format(fieldName, value.toUpperCase()));
                    return;
                }
                target.set(null, newColor);
                success = "\"" + fieldName + TL.COMMAND_CONFIG_COLOURSET + value.toUpperCase() + "\".";
            }

            // Set<?> or other parameterized collection
            else if (target.getGenericType() instanceof ParameterizedType) {
                ParameterizedType targSet = (ParameterizedType) target.getGenericType();
                Type innerType = targSet.getActualTypeArguments()[0];

                // not a Set, somehow, and that should be the only collection we're using in Conf.java
                if (targSet.getRawType() != Set.class) {
                    context.sendMessage(TL.COMMAND_CONFIG_INVALID_COLLECTION.format(fieldName));
                    return;
                }

                // Set<Material>
                else if (innerType == Material.class) {
                    Material newMat = null;
                    try {
                        newMat = Material.valueOf(value.toUpperCase());
                    } catch (IllegalArgumentException ignored) {}
                    if (newMat == null) {
                        context.sendMessage(TL.COMMAND_CONFIG_INVALID_MATERIAL.format(fieldName, value.toUpperCase()));
                        return;
                    }

                    @SuppressWarnings("unchecked") Set<Material> matSet = (Set<Material>) target.get(null);

                    // Material already present, so remove it
                    if (matSet.contains(newMat)) {
                        matSet.remove(newMat);
                        target.set(null, matSet);
                        success = TL.COMMAND_CONFIG_MATERIAL_REMOVED.format(fieldName, value.toUpperCase());
                    }
                    // Material not present yet, add it
                    else {
                        matSet.add(newMat);
                        target.set(null, matSet);
                        success = TL.COMMAND_CONFIG_MATERIAL_ADDED.format(fieldName, value.toUpperCase());
                    }
                }

                // Set<String>
                else if (innerType == String.class) {
                    @SuppressWarnings("unchecked") Set<String> stringSet = (Set<String>) target.get(null);

                    // String already present, so remove it
                    if (stringSet.contains(value)) {
                        stringSet.remove(value);
                        target.set(null, stringSet);
                        success = TL.COMMAND_CONFIG_SET_REMOVED.format(fieldName, value);
                    }
                    // String not present yet, add it
                    else {
                        stringSet.add(value);
                        target.set(null, stringSet);
                        success = TL.COMMAND_CONFIG_SET_ADDED.format(fieldName, value);
                    }
                }

                // Set of unknown type
                else {
                    context.sendMessage(TL.COMMAND_CONFIG_INVALID_TYPESET.format(fieldName));
                    return;
                }
            }

            // unknown type
            else {
                context.sendMessage(TL.COMMAND_CONFIG_ERROR_TYPE.format(fieldName, target.getClass().getName()));
                return;
            }
        } catch (NoSuchFieldException ex) {
            context.sendMessage(TL.COMMAND_CONFIG_ERROR_MATCHING.format(fieldName));
            return;
        } catch (IllegalAccessException ex) {
            context.sendMessage(TL.COMMAND_CONFIG_ERROR_SETTING.format(fieldName, value));
            return;
        }

        if (!success.isEmpty()) {
            if (context.sender instanceof Player) {
                context.sendMessage(success);
                Logger.print(success + TL.COMMAND_CONFIG_LOG.format(context.sender), Logger.PrefixType.DEFAULT);
            } else  // using FactionsPlugin.getInstance().log() instead of sendMessage if run from server console so that "[Factions v#.#.#]" is prepended in server log
            {
                Logger.print(success, Logger.PrefixType.DEFAULT);
            }
        }
        // save change to disk
        Conf.save();
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CONFIG_DESCRIPTION;
    }

    String getAsValidFieldName(String arg){
        // store a lookup map of lowercase field names paired with proper capitalization field names
        // that way, if the person using this command messes up the capitalization, we can fix that
        if (properFieldNames.isEmpty()) {
            Field[] fields = Conf.class.getDeclaredFields();
            for (Field field : fields) {
                properFieldNames.put(field.getName().toLowerCase(), field.getName());
            }
        }

        String field = arg.toLowerCase();
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }

        return properFieldNames.get(field);
    }

    String concatArguments(List<String> args){
        StringBuilder value = new StringBuilder(args.get(0));
        // Begin at the 2nd arg since StringBuilder was initialized containing the 1st one already
        for (int i = 1; i < args.size(); i++) {
            value.append(' ').append(args.get(i));
        }
        return value.toString();
    }
}
