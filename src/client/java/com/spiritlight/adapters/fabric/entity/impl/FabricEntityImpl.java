package com.spiritlight.adapters.fabric.entity.impl;

import com.spiritlight.adapters.fabric.entity.FabricEntity;
import com.spiritlight.adapters.fabric.game.FabricChatComponent;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class FabricEntityImpl implements FabricEntity {
    private final Entity representingEntity;

    public FabricEntityImpl(Entity representingEntity) {
        this.representingEntity = representingEntity;
    }

    @Override
    public FabricChatComponent getName() {
        return FabricChatComponent.of(representingEntity.getName());
    }

    @Override
    public FabricChatComponent getDisplayName() {
        return FabricChatComponent.of(representingEntity.getDisplayName());
    }

    @Override
    public FabricChatComponent getCustomName() {
        return FabricChatComponent.of(representingEntity.getCustomName());
    }

    @Override
    public double getX() {
        return representingEntity.getX();
    }

    @Override
    public double getY() {
        return representingEntity.getY();
    }

    @Override
    public double getZ() {
        return representingEntity.getZ();
    }

    @Override
    public @NotNull Entity getRepresentativeEntity() {
        return representingEntity;
    }
}
