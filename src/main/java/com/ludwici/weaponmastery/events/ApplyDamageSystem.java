package com.ludwici.weaponmastery.events;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.WeaponMastery;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ApplyDamageSystem extends DamageSystems.ApplyDamage {
    @Override
    public void handle(int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl Damage damage) {
        Damage.Source source = damage.getSource();
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

        ItemStack weapon = attacker.getInventory().getItemInHand();
        if (weapon == null) {
            return;
        }

        MasteryComponent masteryComponent = commandBuffer.getComponent(sourceRef, WeaponMastery.getInstance().getMasteryComponent());
        if (masteryComponent == null) {
            return;
        }

        String weaponId = weapon.getItemId();
        int progress = masteryComponent.getProgress(weaponId);

        float bonusMod = 1;
        float newDamage = damage.getAmount();

        if (progress >= 400) {
            bonusMod = 1.5f;
        } else if (progress >= 200) {
            bonusMod = 1.3f;
        } else if (progress >= 100) {
            bonusMod = 1.2f;
        } else if (progress >= 50) {
            bonusMod = 1.15f;
        }

        newDamage *= bonusMod;
        damage.setAmount(Math.round(newDamage));

        Universe.get().sendMessage(Message.raw(String.valueOf(damage.getAmount())));
    }
}
