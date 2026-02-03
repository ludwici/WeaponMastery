package com.ludwici.weaponmastery.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class WeaponMasteryResetCommand extends AbstractPlayerCommand {
    public WeaponMasteryResetCommand(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    RequiredArg<String> playerName = withRequiredArg("player_name", "Player name", ArgTypes.STRING);

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        PlayerRef targetRef = Universe.get().getPlayerByUsername(playerName.get(commandContext), NameMatching.EXACT);
        if (targetRef == null) {
            player.sendMessage(Message.translation("weaponmastery.commands.wm_change.errors.no_player"));
            return;
        }

        MasteryComponent component = store.getComponent(targetRef.getReference(), MasteryComponent.getMasteryComponent());

        if (component == null) {
            return;
        }

        component.progress.clear();
    }
}
