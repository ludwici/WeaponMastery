package com.ludwici.weaponmastery.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.ludwici.weaponmastery.WeaponMastery;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Random;

@SuppressWarnings("removal")
public class MasterySystem {
    private static ComponentType<EntityStore, MasteryComponent> masteryComponentType;
    private static final Random random = new Random();

    public static void handle(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl ItemStack weapon, @NonNullDecl NPCEntity npcComponent) {
        String weaponId = weapon.getItemId();

        if (WeaponMastery.getInstance().getIgnoredWeapons().contains(weaponId)) {
            return;
        }

        if (WeaponMastery.getInstance().getIgnoredRoles().contains(npcComponent.getRoleName())) {
            return;
        }

        if (masteryComponentType == null) {
            masteryComponentType = MasteryComponent.getMasteryComponent();
        }

        Player attacker = store.getComponent(ref, Player.getComponentType());

        MasteryComponent masteryComponent = commandBuffer.getComponent(ref, masteryComponentType);
        if (masteryComponent == null) {
            masteryComponent = new MasteryComponent();
            commandBuffer.addComponent(ref, masteryComponentType, masteryComponent);
        }

        int maxProgressValue = MasteryComponent.MAX_PROGRESS_VALUE;
        int currentProgress = masteryComponent.getProgress(weaponId);

        if (currentProgress < maxProgressValue) {
            double baseChance = (double) (maxProgressValue - currentProgress) / maxProgressValue;
            double chance = baseChance * WeaponMastery.getInstance().getMasteryChance();
            chance = Math.clamp(chance, 0.0, 1.0);
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
}
