package com.ludwici.weaponmastery.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.WeaponMastery;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ApplyDamageSystem extends DamageSystems.ApplyDamage {

    private final boolean handlePerDamage;

    public ApplyDamageSystem(boolean handlePerDamage) {
        this.handlePerDamage = handlePerDamage;
    }

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

        if (handlePerDamage) {
            MasterySystem.handle(sourceRef, store, commandBuffer, weapon);
        }

        MasteryComponent masteryComponent = commandBuffer.getComponent(sourceRef, WeaponMastery.getInstance().getMasteryComponent());
        if (masteryComponent == null) {
            return;
        }

        String weaponId = weapon.getItemId();
        int progress = masteryComponent.getProgress(weaponId);

        float damageBonusMod = 1;
        float newDamage = damage.getAmount();

        if (progress >= 400) {
            damageBonusMod = 1.5f;
        } else if (progress >= 200) {
            damageBonusMod = 1.3f;
        } else if (progress >= 100) {
            damageBonusMod = 1.2f;
        } else if (progress >= 50) {
            damageBonusMod = 1.15f;
        }

        newDamage *= damageBonusMod;
        damage.setAmount(Math.round(newDamage));

//        Universe.get().sendMessage(Message.raw(String.valueOf(damage.getAmount())));

        int energyStat = DefaultEntityStatTypes.getSignatureEnergy();
        EntityStatMap entityStatMapComponent = store.getComponent(sourceRef, EntityStatMap.getComponentType());
        EntityStatValue energyValue = entityStatMapComponent.get(energyStat);
        if (energyValue == null) {
            return;
        }

        float energyBefore = energyValue.get();
        if (energyBefore == 0.0f) {
            return;
        }

        float energyBonus = 0;

        if (progress >= 500) {
            energyBonus = 0.8f;
        } else if (progress >= 300) {
            energyBonus = 0.5f;
        } else if (progress >= 250) {
            energyBonus = 0.4f;
        } else if (progress >= 150) {
            energyBonus = 0.2f;
        }

//        Universe.get().sendMessage(Message.raw("before: " + energyBefore));
        entityStatMapComponent.addStatValue(energyStat, energyBonus);
    }
}
