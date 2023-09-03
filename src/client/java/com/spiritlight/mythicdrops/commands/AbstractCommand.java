package com.spiritlight.mythicdrops.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public abstract class AbstractCommand<S extends FabricClientCommandSource> {

    public abstract LiteralArgumentBuilder<S> getCommand();
}
