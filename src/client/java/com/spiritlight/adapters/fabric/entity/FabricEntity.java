package com.spiritlight.adapters.fabric.entity;

import com.spiritlight.adapters.fabric.AdaptableEntity;
import com.spiritlight.adapters.fabric.entity.impl.FabricEntityImpl;
import com.spiritlight.adapters.fabric.game.FabricChatComponent;
import net.minecraft.entity.Entity;

public interface FabricEntity extends AdaptableEntity<Entity> {

    FabricChatComponent getName();

    FabricChatComponent getDisplayName();

    FabricChatComponent getCustomName();

    double getX();

    double getY();

    double getZ();

    static FabricEntity of(Entity entity) {
        return new FabricEntityImpl(entity);
    }

}
