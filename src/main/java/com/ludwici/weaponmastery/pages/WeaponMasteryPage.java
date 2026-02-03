package com.ludwici.weaponmastery.pages;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ludwici.weaponmastery.components.MasteryComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Map;

public class WeaponMasteryPage extends InteractiveCustomUIPage<MasteryWeaponEventData> {

    private String searchQuery;

    public WeaponMasteryPage(@NonNullDecl PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, MasteryWeaponEventData.CODEC);
        searchQuery = "";
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder uiCommandBuilder, @NonNullDecl UIEventBuilder uiEventBuilder, @NonNullDecl Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/WeaponMasteryPage.ui");
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.ValueChanged, "#WeaponIDInput", new EventData().append("@WeaponIDInput", "#WeaponIDInput.Value"));
        buildWeaponList(ref, uiCommandBuilder, store);
    }

    @Override
    public void handleDataEvent(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store, @NonNullDecl MasteryWeaponEventData data) {
        searchQuery = data.weaponIDInput.trim().toLowerCase();
        UICommandBuilder commandBuilder = new UICommandBuilder();
        buildWeaponList(ref, commandBuilder, store);
        sendUpdate(commandBuilder);
    }

    private void buildWeaponList(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder uiCommandBuilder, @NonNullDecl Store<EntityStore> store) {
        uiCommandBuilder.clear("#MasteryList");
        MasteryComponent masteryComponent = store.getComponent(ref, MasteryComponent.getMasteryComponent());
        if (masteryComponent == null) {
            return;
        }
        int idx = 0;
        ItemStack weapon;
        Item weaponItem;
        String selector;
//        float value;

        for (Map.Entry<String, Integer> entry : masteryComponent.progress.entrySet()) {
            weapon = new ItemStack(entry.getKey());
            weaponItem = weapon.getItem();
            if (weaponItem == Item.UNKNOWN) {
                continue;
            }

//            if (!(searchQuery.isEmpty()) && !entry.getKey().contains(searchQuery)) {
            String weaponName = Message.translation(weaponItem.getTranslationKey()).getAnsiMessage().toLowerCase();
            if (!(searchQuery.isEmpty()) && !weaponName.contains(searchQuery)) {
                continue;
            }
            uiCommandBuilder.append("#MasteryList", "Pages/MasteryEntry.ui");
            selector = "#MasteryList[" + idx + "] ";
            uiCommandBuilder.set(selector + "#WeaponName.TextSpans", Message.translation(weaponItem.getTranslationKey()));
            uiCommandBuilder.set(selector + "#WeaponItem.ItemId", entry.getKey());
//            value = (float) entry.getValue() / 500;
            uiCommandBuilder.set(selector + "#MasteryProgress.Value", 0);
//            uiCommandBuilder.set(selector + "#MasteryProgressTexture.Value", value);
            {
                Anchor anchor = new Anchor();
                anchor.setWidth(Value.of(entry.getValue() * 300 / 500));
                anchor.setLeft(Value.of(-3));
                uiCommandBuilder.setObject(selector + "#MasteryProgressTexture.Anchor", anchor);
            }
            uiCommandBuilder.set(selector + "#CurrentProgress.TextSpans", Message.raw(entry.getValue() + " / 500"));

            for (int tier = 0; tier <= 7; tier++) {
                uiCommandBuilder.append(selector + "#TiersPanel", "Pages/TierEntry.ui");
                String tierSelector = selector + "#TiersPanel[" + tier + "]";
                Anchor anchor = new Anchor();
                int left = MasteryComponent.tierByProgress.get(tier) * 300 / 500 - 10;
                anchor.setBottom(Value.of(0));
                anchor.setWidth(Value.of(20));
                anchor.setHeight(Value.of(20));
                anchor.setLeft(Value.of(left));
                uiCommandBuilder.setObject(tierSelector + ".Anchor", anchor);
                Message tooltipText = Message.translation("weaponmastery.mastery.tier." + tier + ".desc");
                uiCommandBuilder.set(tierSelector + ".TooltipTextSpans", tooltipText);
                uiCommandBuilder.set(tierSelector + " #Mark.Visible", MasteryComponent.tierUnlocked(tier, entry.getValue()));
            }
            idx++;
        }
    }
}
