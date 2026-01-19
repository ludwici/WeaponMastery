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
import com.hypixel.hytale.server.core.util.NotificationUtil;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.ludwici.weaponmastery.WeaponMastery;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class KillEventSystem extends DeathSystems.OnDeathSystem {
    private final ComponentType<EntityStore, MasteryComponent> masteryComponentType;
    private final Random random = new Random();

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
        if (weapon == null || weapon.getItem().getWeapon() == null) {
            return;
        }

        MasteryComponent masteryComponent = commandBuffer.getComponent(sourceRef, masteryComponentType);
        if (masteryComponent == null) {
            masteryComponent = new MasteryComponent();
            commandBuffer.addComponent(sourceRef, masteryComponentType, masteryComponent);
        }

        String weaponId = weapon.getItemId();
        int maxProgressValue = MasteryComponent.MAX_PROGRESS_VALUE;
        int currentProgress = masteryComponent.getProgress(weaponId);

        if (currentProgress < maxProgressValue) {
            double chance = (double) (maxProgressValue - currentProgress) / maxProgressValue;
            double val = random.nextDouble(0.0, 1.0);
//            Universe.get().sendMessage(Message.raw("Шанс: " + chance + "/" + val));

            if (val < chance) {
                int rate = WeaponMastery.getInstance().getMasteryRate();
                masteryComponent.addProgress(weaponId, rate);
                if (WeaponMastery.getInstance().isShowImproveMasteryNotification()) {
                    int newProgress = masteryComponent.getProgress(weaponId);
                    int currentTier = MasteryComponent.getTier(currentProgress);
                    int newTier = MasteryComponent.getTier(newProgress);
                    if (newTier > currentTier) {
                        var packetHandler = attacker.getPlayerRef().getPacketHandler();
                        var primaryMessage = Message.translation("weaponmastery.mastery.notification.title").bold(true).param("current", newProgress).param("maximum", maxProgressValue);
                        var secondaryMessage = Message.translation("weaponmastery.mastery.notification.tier." + newTier + ".desc").bold(true);
                        var icon = weapon.toPacket();
                        NotificationUtil.sendNotification(packetHandler, primaryMessage, secondaryMessage, icon);
                    }
                }
//                String playerName = attacker.getDisplayName();
//                Universe.get().sendMessage(Message.raw(playerName + " убил нипа с помощью " + weapon.getItemId() + " " + masteryComponent.getProgress(weaponId) + "/100"));
            }
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }
}
