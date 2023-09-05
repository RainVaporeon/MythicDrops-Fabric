package com.spiritlight.mythicdrops.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.spiritlight.mythicdrops.Client;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Locale;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.minecraft.util.Formatting.*;

public class MainCommand extends AbstractCommand<FabricClientCommandSource> {

    private static final String PREFIX = DARK_PURPLE + "[" + LIGHT_PURPLE + "MythicDrops" + DARK_PURPLE + "]";

    private static final String PREFIX_NO_BRACKET = LIGHT_PURPLE + "MythicDrops";

    private static final String COMMAND_BASE = DARK_PURPLE + "/" + LIGHT_PURPLE + "mythic " + WHITE;

    private static final String CONCAT = YELLOW + " - " + GRAY;

    private static final String SUPPORT = LIGHT_PURPLE + "Support me at: " + AQUA + "https://ko-fi.com/orispirit";

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("mythic")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(Text.of(String.format("""
                            %s
                            %s
                            %s
                            %s
                            %s
                            %s
                            %s
                            
                            %s""",
                            PREFIX_NO_BRACKET + CONCAT + "Command Help",
                            COMMAND_BASE + "toggle" + CONCAT + "Toggles the mod",
                            COMMAND_BASE + "star <name>" + CONCAT + "Stars an item",
                            COMMAND_BASE + "unstar <name>" + CONCAT + "Un-stars an item",
                            COMMAND_BASE + "stars" + CONCAT + "Shows all starred names",
                            COMMAND_BASE + "hide" + CONCAT + "Hides the dropped item name",
                            COMMAND_BASE + "ignoreIdentified" + CONCAT + "Toggle ignore identified items",

                            SUPPORT
                            )));
                    return 1;
                })
                .then(literal("toggle").executes(ctx -> {
                    boolean v = Client.getDatabase().toggleActive();
                    ctx.getSource().sendFeedback(Text.of(
                            PREFIX_NO_BRACKET + CONCAT + "The mod has been turned " + WHITE + (v ? "on" : "off") + GRAY + "."
                    ));
                    return 1;
                }))
                .then(literal("hide").executes(ctx -> {
                    boolean v = Client.getDatabase().toggleSecret();
                    ctx.getSource().sendFeedback(Text.of(
                            PREFIX_NO_BRACKET + CONCAT + "Hide item name has been turned " + WHITE + (v ? "on" : "off") + GRAY + "."
                    ));
                    return 1;
                }))
                .then(literal("ignoreIdentified").executes(ctx -> {
                    boolean v = Client.getDatabase().toggleIgnoreId();
                    ctx.getSource().sendFeedback(Text.of(
                            PREFIX_NO_BRACKET + CONCAT + "Ignore identified item has been turned " + WHITE + (v ? "on" : "off") + GRAY + "."
                    ));
                    return 1;
                }))
                .then(literal("star")
                        .then(argument("value", StringArgumentType.greedyString()).executes(ctx -> {
                    // Calling String.valueOf for null safety
                    String val = String.valueOf(ctx.getArgument("value", String.class));
                    boolean successful = Client.getDatabase().addWhitelist(val);

                    if(successful) {
                        ctx.getSource().sendFeedback(Text.of(
                                PREFIX_NO_BRACKET + CONCAT + "Successfully added " + RESET + val + GRAY + " into the starred list. "
                        ));
                        return 1;
                    } else {
                        ctx.getSource().sendFeedback(Text.of(
                                PREFIX_NO_BRACKET + CONCAT + "Failed to add " + RESET + val + GRAY + " into the starred list. " +
                                        "This entry might be present in the list already."
                        ));
                        return 0;
                    }
                })))
                .then(literal("unstar")
                        .then(argument("value", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    String current = getValueElse(ctx);
                                    for(String value : Client.getDatabase().getWhitelistedItems().stream().filter(t -> t.toLowerCase(Locale.ROOT).startsWith(current.toLowerCase(Locale.ROOT))).toList()) {
                                        builder.suggest(value);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                    // Calling String.valueOf for null safety
                    String val = String.valueOf(ctx.getArgument("value", String.class));
                    boolean successful = Client.getDatabase().removeWhitelist(val);

                    if(successful) {
                        ctx.getSource().sendFeedback(Text.of(
                                PREFIX_NO_BRACKET + CONCAT + "Successfully removed " + RESET + val + GRAY + " from the starred list."
                        ));
                        return 1;
                    } else {
                        ctx.getSource().sendFeedback(Text.of(
                                PREFIX_NO_BRACKET + CONCAT + "Failed to remove " + RESET + val + GRAY + " from the starred list." +
                                        "This entry might not be present."
                        ));
                        return 0;
                    }
                })))
                .then(literal("stars").executes(ctx -> {
                    List<String> elements = Client.getDatabase().getWhitelistedItems();
                    int size = elements.size();
                    ctx.getSource().sendFeedback(Text.of(String.format("""
                            %s
                            %s
                            %s""",
                            PREFIX + " There are currently " + size + " " + (size == 1 ? "entry" : "entries")  +":",
                            elements,
                            GRAY + "Use /mythic unstar <entry> to remove an entry.")
                    ));
                    return 1;
                }));
    }

    private static String getValueElse(CommandContext<?> ctx) {
        try {
            return ctx.getArgument("value", String.class);
        } catch (Exception ex) {
            return "";
        }
    }
}
