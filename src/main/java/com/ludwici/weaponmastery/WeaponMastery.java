package com.ludwici.weaponmastery;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.components.MasteryComponent;
import com.ludwici.weaponmastery.events.KillEventSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class WeaponMastery extends JavaPlugin {
    private ComponentType<EntityStore, MasteryComponent> masteryComponent;

    public WeaponMastery(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();
        getCommandRegistry().registerCommand(new Command("test_command", "desc", false));

        masteryComponent = getEntityStoreRegistry().registerComponent(MasteryComponent.class, "Mastery", MasteryComponent.CODEC);

        getEntityStoreRegistry().registerSystem(new KillEventSystem(masteryComponent));
    }

    public ComponentType<EntityStore, MasteryComponent> getMasteryComponent() {
        return masteryComponent;
    }

}