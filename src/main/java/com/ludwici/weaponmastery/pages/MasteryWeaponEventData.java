package com.ludwici.weaponmastery.pages;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MasteryWeaponEventData {
    public String weaponIDInput;

    public static final BuilderCodec<MasteryWeaponEventData> CODEC = BuilderCodec.<MasteryWeaponEventData>builder(MasteryWeaponEventData.class, MasteryWeaponEventData::new)
            .append(new KeyedCodec<>("@WeaponIDInput", Codec.STRING),
                    (c, v) -> c.weaponIDInput = v, c -> c.weaponIDInput)
            .add()
            .build();
}
