package com.spiritlight.mythicdrops;

import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBus;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBusAdapter;
import com.spiritlight.adapters.fabric.misc.event.events.game.ClientTickEndEvent;
import com.spiritlight.adapters.fabric.misc.event.events.game.RunnableExecutionEvent;
import com.spiritlight.mythicdrops.commands.MainCommand;
import com.spiritlight.mythicdrops.data.Database;
import com.spiritlight.mythicdrops.handlers.CommandRegistrar;
import com.spiritlight.mythicdrops.handlers.EntityHandler;
import com.spiritlight.mythicdrops.utils.ItemDetails;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends EventBusAdapter implements ClientModInitializer {
	public static final long EXECUTION_CODE = new Random().nextLong();

	private static final Database DB = new Database();

	private static final Map<String, ItemDetails> database;

	private final Queue<Runnable> queue = new LinkedBlockingQueue<>();

	private static boolean created = false;

	static {
		Logger INIT = LogManager.getLogger("MythicDrops/Init");

		final File databaseSave = new File("config/MythicDrops119.json");

		database = Internals.loadItems();

		try {
			DB.deserialize(databaseSave);
		} catch (IOException ex) {
			INIT.error("Failed to load configurations: ", ex);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				DB.serialize(databaseSave);
			} catch (IOException ex) {
				INIT.error("Failed to save configurations: ", ex);
			}
		}
		));
	}

	public Client() {
		if(created) throw new AssertionError("MythicDrops was initialized twice!");
		created = true;
	}

	public static Database getDatabase() {
		return DB;
	}

	public static ItemDetails getItem(String in) {
		return database.getOrDefault(in, ItemDetails.UNKNOWN);
	}

	@Override
	public void onInitializeClient() {
		Internals.initialize();
		EventBus.instance.subscribe(this);
		EventBus.instance.subscribe(CommandRegistrar.INSTANCE);

		CommandRegistrar.INSTANCE.registerCommand(new MainCommand());

		EntityHandler handler = new EntityHandler();
		handler.start();
	}

	@Override
	public void onRunnableExecution(RunnableExecutionEvent event) {
		if(event.checkKey(EXECUTION_CODE)) {
			queue.add(event.getRunnable(EXECUTION_CODE));
		}
	}

	@Override
	public void onClientTickEnd(ClientTickEndEvent event) {
		for(int i = 0; i < 5 && !queue.isEmpty(); i++) {
			try {
				queue.poll().run();
			} catch (Exception e) {
				LogManager.getLogger("MythicDrops").error("Failed to handle runnable: ", e);
			}
		}
	}
}