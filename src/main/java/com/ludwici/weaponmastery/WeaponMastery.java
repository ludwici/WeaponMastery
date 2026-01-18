package com.ludwici.weaponmastery;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.commands.WeaponMasteryChangeCommand;
import com.ludwici.weaponmastery.commands.WeaponMasteryOpenCommand;
import com.ludwici.weaponmastery.components.MasteryComponent;
import com.ludwici.weaponmastery.events.ApplyDamageSystem;
import com.ludwici.weaponmastery.events.KillEventSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class WeaponMastery extends JavaPlugin {
    private static WeaponMastery instance;
    private ComponentType<EntityStore, MasteryComponent> masteryComponent;

    public WeaponMastery(@NonNullDecl JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static WeaponMastery getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        super.setup();
        getCommandRegistry().registerCommand(new WeaponMasteryChangeCommand("wm_change", "weaponmastery.commands.wm_change.desc", false));
        getCommandRegistry().registerCommand(new WeaponMasteryOpenCommand("wm", "", false));
        masteryComponent = getEntityStoreRegistry().registerComponent(MasteryComponent.class, "Mastery", MasteryComponent.CODEC);

        getEntityStoreRegistry().registerSystem(new KillEventSystem(masteryComponent));
        getEntityStoreRegistry().registerSystem(new ApplyDamageSystem());
    }

    public ComponentType<EntityStore, MasteryComponent> getMasteryComponent() {
        return masteryComponent;
    }

}