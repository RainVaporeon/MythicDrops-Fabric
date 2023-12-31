package com.spiritlight.mythicdrops;

import com.google.gson.*;
import com.spiritlight.adapters.fabric.entity.FabricEntity;
import com.spiritlight.adapters.fabric.misc.connection.HttpRequests;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBus;
import com.spiritlight.adapters.fabric.misc.event.events.game.ClientTickEndEvent;
import com.spiritlight.adapters.fabric.misc.event.events.game.entity.EntityTrackingEvent;
import com.spiritlight.mythicdrops.api.Query;
import com.spiritlight.mythicdrops.utils.ItemDetails;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.HashMap;
import java.util.Map;

public class Internals {
    /**
     * Initializes the bus adapter
     */
    static void initialize() {
        ClientEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            EventBus.instance.fire(new EntityTrackingEvent(FabricEntity.of(entity)));
        }));

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            EventBus.instance.fire(new ClientTickEndEvent());
        }));
    }

    /**
     * Utility to fetch all items
     */
    static Map<String, ItemDetails> loadItems() {
        Map<String, ItemDetails> dataset = new HashMap<>();

        Gson gson = new Gson();

        JsonArray items = gson.fromJson(HttpRequests.get(Query.ITEM_API), JsonObject.class)
                .getAsJsonArray("items");

        items.forEach(element -> {
            ItemDetails details = ItemDetails.fromJson(element);
            dataset.put(details.getName(), details);
        });

        return dataset;
    }
}
