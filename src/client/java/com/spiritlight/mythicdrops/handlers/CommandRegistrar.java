package com.spiritlight.mythicdrops.handlers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBusAdapter;
import com.spiritlight.adapters.fabric.misc.event.events.game.ClientCommandInitializationEvent;
import com.spiritlight.mythicdrops.commands.AbstractCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import org.apache.logging.log4j.LogManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandRegistrar extends EventBusAdapter {

    private CommandRegistrar(){}

    public static final CommandRegistrar INSTANCE = new CommandRegistrar();

    private final Queue<LiteralArgumentBuilder<FabricClientCommandSource>> sourceSet = new LinkedBlockingQueue<>();

    @Override
    public void onClientCommandInitialization(ClientCommandInitializationEvent event) {
        if(ClientCommandInternals.getActiveDispatcher() == null) {
            LogManager.getLogger("MythicDrops/ClientCommandRegistrar").error("No Active Dispatcher");
            return;
        }
        for(LiteralArgumentBuilder<FabricClientCommandSource> o : sourceSet) {
            ClientCommandInternals.getActiveDispatcher().register(o);
        }
    }

    public void registerCommand(AbstractCommand cmd) {
        this.sourceSet.add(cmd.getCommand());
    }

    public void removeCommand(AbstractCommand cmd) {
        this.sourceSet.remove(cmd.getCommand());
    }
}
