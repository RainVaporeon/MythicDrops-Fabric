package com.spiritlight.mythicdrops.handlers;

import com.spiritlight.adapters.fabric.entity.FabricEntity;
import com.spiritlight.adapters.fabric.entity.FabricPlayer;
import com.spiritlight.adapters.fabric.game.FabricChatComponent;
import com.spiritlight.adapters.fabric.game.FabricClient;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBus;
import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBusAdapter;
import com.spiritlight.adapters.fabric.misc.event.events.game.entity.EntityTrackingEvent;
import com.spiritlight.mythicdrops.Client;
import com.spiritlight.mythicdrops.utils.ItemRarity;
import com.spiritlight.mythicdrops.utils.MainThread;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
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

        boolean handleEffects;

        boolean preIdentified = preIdentified(item.getStack());

        if(preIdentified && Client.getDatabase().ignoreIdentified()) return;

        if(Client.getItem(name).getRarity() == ItemRarity.MYTHIC) {
            handleEffects = true;
            FabricClient.getInstance().getPlayer().sendMessage(
                    FabricChatComponent.of(
                            Formatting.DARK_PURPLE + "[" + Formatting.LIGHT_PURPLE + "!" + Formatting.DARK_PURPLE + "] " +
                                    (preIdentified ? Formatting.WHITE + "Identified " : "") +
                                    Formatting.LIGHT_PURPLE + "Mythic Item " + Formatting.DARK_PURPLE + displayName + Formatting.LIGHT_PURPLE +
                                    " has dropped at " + Formatting.GREEN + String.format("%d, %d, %d!",
                                    (int) entity.getX(), (int) entity.getY(), (int) entity.getZ())
                    )
            );
        } else if (Client.getDatabase().inWhitelist(name)) {
            handleEffects = true;
            FabricClient.getInstance().getPlayer().sendMessage(
                    FabricChatComponent.of(
                            Formatting.AQUA + "[" + Formatting.YELLOW + "!" + Formatting.AQUA + "] " +
                                    (preIdentified ? Formatting.WHITE + "Identified " : "") +
                                    Formatting.AQUA + "Starred Item " + Formatting.YELLOW + displayName + Formatting.AQUA +
                                    " has dropped at " + Formatting.GREEN + String.format("%d, %d, %d!",
                                    (int) entity.getX(), (int) entity.getY(), (int) entity.getZ())
                    )
            );
        } else {
            handleEffects = false;
        }

        if(handleEffects) {
            entity.getRepresentativeEntity().setGlowing(true);
            World world = FabricClient.getInstance().getRepresentativeEntity().world;
            FabricPlayer player = FabricClient.getInstance().getPlayer();

            if(world == null || player == null) return;

            world.playSound(player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER,
                    1.0f, 1.0f, false);
        }
    }

    private static boolean preIdentified(ItemStack stack) {
        try {
            return stack.getOrCreateNbt().getCompound("display").toString().contains("identifications");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param entity
     * @return true if the entity matches scanning preconditions
     */
    private static boolean checkPreconditions(FabricEntity entity) {
        return entity.getRepresentativeEntity() instanceof ItemEntity;
    }

    public void start() {
        EventBus.instance.subscribe(this);
    }

    public void stop() {
        EventBus.instance.unsubscribe(this);
    }
}
