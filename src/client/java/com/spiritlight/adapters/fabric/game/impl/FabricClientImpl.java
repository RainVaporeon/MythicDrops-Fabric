package com.spiritlight.adapters.fabric.game.impl;

import com.spiritlight.adapters.fabric.entity.FabricEntity;
import com.spiritlight.adapters.fabric.entity.FabricPlayer;
import com.spiritlight.adapters.fabric.game.FabricClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class FabricClientImpl implements FabricClient {

    @Override
    public FabricPlayer getPlayer() {
        return FabricPlayer.from(MinecraftClient.getInstance().player);
    }

    @Override
    public List<FabricEntity> getEntities(Predicate<FabricEntity> filter) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return List.of();
        return StreamSupport.stream(world.getEntities().spliterator(), false)
                .map(FabricEntity::of).filter(filter).toList();
    }

    @Override
    public @NotNull MinecraftClient getRepresentativeEntity() {
        return MinecraftClient.getInstance();
    }
}
