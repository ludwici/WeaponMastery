package com.ludwici.weaponmastery.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class WeaponMasteryCleanUnkCommand extends AbstractPlayerCommand {
    public WeaponMasteryCleanUnkCommand(@NonNullDecl String name, @NonNullDecl String description) {
        super(name, description, false);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        MasteryComponent component = store.getComponent(ref, MasteryComponent.getMasteryComponent());

        if (component == null) {
            return;
        }

        component.progress.entrySet().removeIf(entry -> new ItemStack(entry.getKey()).getItem() == Item.UNKNOWN);
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
}
