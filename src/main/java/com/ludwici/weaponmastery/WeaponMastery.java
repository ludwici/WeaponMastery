package com.ludwici.weaponmastery;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.ludwici.weaponmastery.commands.WeaponMasteryChangeCommand;
import com.ludwici.weaponmastery.commands.WeaponMasteryOpenCommand;
import com.ludwici.weaponmastery.components.MasteryComponent;
import com.ludwici.weaponmastery.config.MasteryConfig;
import com.ludwici.weaponmastery.events.ApplyDamageSystem;
import com.ludwici.weaponmastery.events.KillEventSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.nio.file.Files;

public class WeaponMastery extends JavaPlugin {
    private static WeaponMastery instance;

    private final Config<MasteryConfig> config;

    private ComponentType<EntityStore, MasteryComponent> masteryComponent;

    public WeaponMastery(@NonNullDecl JavaPluginInit init) {
        super(init);
        instance = this;
        config = withConfig("WeaponMastery", MasteryConfig.CODEC);
    }

    public static WeaponMastery getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        super.setup();
        if (!Files.exists(getDataDirectory())) {
            config.save().thenRun(() -> getLogger().atInfo().log("WeaponMastery saved default config"));
        }
        getCommandRegistry().registerCommand(new WeaponMasteryChangeCommand("wm_change", "weaponmastery.commands.wm_change.desc", false));
        getCommandRegistry().registerCommand(new WeaponMasteryOpenCommand("wm", "", false));
        masteryComponent = getEntityStoreRegistry().registerComponent(MasteryComponent.class, "Mastery", MasteryComponent.CODEC);

        getEntityStoreRegistry().registerSystem(new KillEventSystem(masteryComponent));
        getEntityStoreRegistry().registerSystem(new ApplyDamageSystem());
    }

    public ComponentType<EntityStore, MasteryComponent> getMasteryComponent() {
        return masteryComponent;
    }

    public boolean isShowImproveMasteryNotification() {
        return config.get().isShowImproveMasteryNotification();
    }

    public int getMasteryRate() {
        return config.get().getMasteryRate();
    }
}