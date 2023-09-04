package com.spiritlight.mythicdrops.handlers;

import com.spiritlight.adapters.fabric.entity.FabricEntity;
import com.spiritlight.adapters.fabric.game.FabricChatComponent;
import com.spiritlight.adapters.fabric.game.FabricClient;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBus;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBusAdapter;
import com.spiritlight.adapters.fabric.misc.event.events.game.entity.EntityTrackingEvent;
import com.spiritlight.mythicdrops.Client;
import com.spiritlight.mythicdrops.utils.ItemRarity;
import com.spiritlight.mythicdrops.utils.MainThread;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EntityHandler extends EventBusAdapter {

    private static final ScheduledExecutorService SHARED_EXECUTOR = Executors.newScheduledThreadPool(8);

    @Override
    public void onEntityTracking(EntityTrackingEvent event) {
        if(!Client.getDatabase().isActive()) return;
        SHARED_EXECUTOR.schedule(() -> MainThread.run(() -> this.scanEntity(event.getEntity()))
                , 50, TimeUnit.MILLISECONDS);
    }

    private void scanEntity(FabricEntity entity) {
        if(!checkPreconditions(entity)) return;
        ItemEntity item = (ItemEntity) entity.getRepresentativeEntity();
        String name = item.getStack().getName().getString();
        String displayName = Client.getDatabase().isSecret() ? "[???]" : name;
        if(Client.getItem(name).getRarity() == ItemRarity.MYTHIC) {
            item.setGlowing(true);
            FabricClient.getInstance().getPlayer().sendMessage(
                    FabricChatComponent.of(
                            Formatting.DARK_PURPLE + "[" + Formatting.LIGHT_PURPLE + "!" + Formatting.DARK_PURPLE + "] " +
                                    Formatting.LIGHT_PURPLE + "Mythic Item " + Formatting.DARK_PURPLE + displayName + Formatting.LIGHT_PURPLE +
                                    " has dropped at " + Formatting.GREEN + String.format("%f, %f, %f!",
                                    entity.getX(), entity.getY(), entity.getZ())
                    )
            );
        } else if (Client.getDatabase().inWhitelist(name)) {
            item.setGlowing(true);
            FabricClient.getInstance().getPlayer().sendMessage(
                    FabricChatComponent.of(
                            Formatting.AQUA + "[" + Formatting.YELLOW + "!" + Formatting.AQUA + "] " +
                                    Formatting.AQUA + "Starred Item " + Formatting.YELLOW + displayName + Formatting.AQUA +
                                    " has dropped at " + Formatting.GREEN + String.format("%f, %f, %f!",
                                    entity.getX(), entity.getY(), entity.getZ())
                    )
            );
        }
    }

    /**
     *
     * @param entity
     * @return true if the entity matches scanning preconditions
     */
    private boolean checkPreconditions(FabricEntity entity) {
        return entity.getRepresentativeEntity() instanceof ItemEntity;
    }

    public void start() {
        EventBus.instance.subscribe(this);
    }

    public void stop() {
        EventBus.instance.unsubscribe(this);
    }
}
