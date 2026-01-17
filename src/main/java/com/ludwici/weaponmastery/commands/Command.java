package com.ludwici.weaponmastery.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.ludwici.weaponmastery.pages.WeaponMasteryPage;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class Command extends AbstractPlayerCommand {

    public Command(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
//        EventTitleUtil.showEventTitleToPlayer(playerRef, Message.raw("Title"), Message.raw("Second"), true);
        Player player = store.getComponent(ref, Player.getComponentType());
        WeaponMasteryPage page = new WeaponMasteryPage(playerRef);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
