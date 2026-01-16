package com.ludwici.weaponmastery.events;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.*;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class KillEventSystem extends DeathSystems.OnDeathSystem {
    private final ComponentType<EntityStore, MasteryComponent> masteryComponentType;

    public KillEventSystem(ComponentType<EntityStore, MasteryComponent> masteryComponent) {
        this.masteryComponentType = masteryComponent;
    }

    @Override
    public void onComponentAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl DeathComponent deathComponent, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        DamageCause deathCause = deathComponent.getDeathCause();
        if (deathCause != DamageCause.PHYSICAL && deathCause != DamageCause.PROJECTILE) {
            return;
        }

        Damage deathInfo = deathComponent.getDeathInfo();
        if (deathInfo == null) {
            return;
        }

        Damage.Source source = deathInfo.getSource();
        if (!(source instanceof Damage.EntitySource entitySource)) {
            return;
        }


        Ref<EntityStore> sourceRef = entitySource.getRef();
        if (!sourceRef.isValid()) {
            return;
        }

        Player attacker = store.getComponent(sourceRef, Player.getComponentType());

        if (attacker == null) {
            return;
        }

        NPCEntity npcComponent = commandBuffer.getComponent(ref, NPCEntity.getComponentType());
        if (npcComponent == null) {
            return;
        }

        ItemStack weapon = attacker.getInventory().getItemInHand();
        if (weapon == null) {
            return;
        }

        MasteryComponent masteryComponent = commandBuffer.getComponent(sourceRef, masteryComponentType);
        if (masteryComponent == null) {
            masteryComponent = new MasteryComponent();
            commandBuffer.addComponent(sourceRef, masteryComponentType, masteryComponent);
            Universe.get().sendMessage(Message.raw("init mastery"));
        }

        String weaponId = weapon.getItemId();

        masteryComponent.addProgress(weaponId);
        String playerName = attacker.getDisplayName();
        Universe.get().sendMessage(Message.raw(playerName + " убил нипа с помощью " + weapon.getItemId() + " " + masteryComponent.getProgress(weaponId) + "/100"));
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }
}
