package com.ludwici.weaponmastery.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.ObjectMapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.*;

public class MasteryComponent implements Component<EntityStore> {
    public Map<String, Integer> progress;
    public static int MAX_PROGRESS_VALUE = 500;

    public static final Map<Integer, Integer> tierByProgress = Map.of(
          0, 50,
          1, 100,
          2, 150,
          3, 200,
          4, 250,
          5, 300,
          6, 400,
          7, 500
    );

    public static boolean tierUnlocked(int tier, int currentProgress) {
        Integer required = tierByProgress.get(tier);
        if (required == null) {
            return false;
        }
        return currentProgress >= required;
    }

    public static final BuilderCodec<MasteryComponent> CODEC = BuilderCodec.builder(MasteryComponent.class, MasteryComponent::new)
            .append(new KeyedCodec<>("Progress", new ObjectMapCodec<>(
                    Codec.INTEGER, LinkedHashMap::new, key -> key, str -> str
                    )),
                    (c, v) -> c.progress = new HashMap<>(v), c -> c.progress)
            .add()
            .build();

    public MasteryComponent() {
        progress = new HashMap<>();
    }

    public MasteryComponent(MasteryComponent other) {
        progress = new HashMap<>(other.progress);
    }

    public void addProgress(String weaponId) {
        addProgress(weaponId, 1);
    }

    public void addProgress(String weaponId, int value) {
        progress.merge(weaponId, value, (oldVal, add) -> Math.max(0, Math.min(MAX_PROGRESS_VALUE, oldVal + add)));
    }

    public int getProgress(String weaponId) {
        return progress.getOrDefault(weaponId, 0);
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new MasteryComponent(this);
    }
}
