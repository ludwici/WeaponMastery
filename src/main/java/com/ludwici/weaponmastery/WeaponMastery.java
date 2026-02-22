package com.ludwici.weaponmastery;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.ludwici.weaponmastery.commands.WeaponMasteryChangeCommand;
import com.ludwici.weaponmastery.commands.WeaponMasteryCleanUnkCommand;
import com.ludwici.weaponmastery.commands.WeaponMasteryOpenCommand;
import com.ludwici.weaponmastery.commands.WeaponMasteryResetCommand;
import com.ludwici.weaponmastery.components.MasteryComponent;
import com.ludwici.weaponmastery.config.MasteryConfig;
import com.ludwici.weaponmastery.systems.ApplyDamageSystem;
import com.ludwici.weaponmastery.systems.KillEventSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.nio.file.Files;
import java.util.List;

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
        new HStats("a6f7662a-bc45-4c48-9dc8-a1fc78149689", "1.5.1");
        if (!Files.exists(getDataDirectory())) {
            config.save().thenRun(() -> getLogger().atInfo().log("WeaponMastery saved default config"));
        }

        getCommandRegistry().registerCommand(new WeaponMasteryChangeCommand("wm_change", "weaponmastery.commands.wm_change.desc", false));
        getCommandRegistry().registerCommand(new WeaponMasteryOpenCommand("wm", "", false));
        getCommandRegistry().registerCommand(new WeaponMasteryCleanUnkCommand("wm_clean", ""));
        getCommandRegistry().registerCommand(new WeaponMasteryResetCommand("wm_reset_all", "", true));
        masteryComponent = getEntityStoreRegistry().registerComponent(MasteryComponent.class, "Mastery", MasteryComponent.CODEC);

        String mode = getMasteryMode();
        boolean handlePerDamage = false;

        switch (mode) {
            case "DAMAGE" -> handlePerDamage = true;
            default -> getEntityStoreRegistry().registerSystem(new KillEventSystem());
        }

        getEntityStoreRegistry().registerSystem(new ApplyDamageSystem(handlePerDamage));
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

    public float getMasteryChance() {
        return config.get().getMasteryChance();
    }

    public String getMasteryMode() {
        return config.get().getMasteryMode();
    }

    public List<String> getIgnoredWeapons() {
        return config.get().getIgnoredWeapons();
    }

    public List<String> getIgnoredRoles() {
        return config.get().getIgnoredRoles();
    }
}