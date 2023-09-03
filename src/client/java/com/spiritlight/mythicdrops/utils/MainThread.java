package com.spiritlight.mythicdrops.utils;

import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBus;
import com.spiritlight.adapters.fabric.misc.event.events.game.RunnableExecutionEvent;
import com.spiritlight.mythicdrops.Client;

public class MainThread {

    /**
     * Executes the runnable in the main thread, since the main thread has hooked an
     * event listener from the main thread, the main thread will be running this
     * synchronously with the game code, preventing concurrency issues.
     * @param runnable the runnable
     */
    public static void run(Runnable runnable) {
        EventBus.instance.fire(new RunnableExecutionEvent(Client.EXECUTION_CODE, runnable));
    }

}
