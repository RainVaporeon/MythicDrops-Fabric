package com.spiritlight.mythicdrops.mixin.client;

import com.spiritlight.adapters.fabric.misc.event.events.bus.EventBus;
import com.spiritlight.adapters.fabric.misc.event.events.game.ClientCommandInitializationEvent;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommandInternals.class)
public class FabricClientCommandInternalsMixin {

    @Inject(method = "finalizeInit", at = @At("HEAD"), remap = false)
    private static void finalizeInit(CallbackInfo ci) {
        EventBus.instance.fire(new ClientCommandInitializationEvent());
    }
}
