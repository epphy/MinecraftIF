package com.mif.mif.core.feature;

import com.mif.mif.core.feature.addon.creeperexplosiongrief.CreeperAntiGriefAddon;
import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureRegistry {
    private static final Map<FeatureId, FeatureFactory<Feature>> REGISTERED_FEATURES = new HashMap<>();
    private static FeatureRegistry instance;

    public static FeatureRegistry getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized yet");
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new FeatureRegistry();
        }

        instance.registerAllFeatures();
        MIFLogger.info(instance, "initialized");
    }

    private void registerAllFeatures() {
        // Fixes

        // Addons
        registerFeature(FeatureId.CREEPER_ANTI_GRIEF_ADDON, featureContext -> {
            final var addon = new CreeperAntiGriefAddon();
            addon.enable(featureContext);
            return addon;
        });
    }

    public boolean registerFeature(@NotNull FeatureId featureId, @NotNull FeatureFactory<Feature> featureFactory) {
        final boolean absent = !isFeatureRegistered(featureId);
        if (absent) REGISTERED_FEATURES.put(featureId, featureFactory);
        return absent;
    }

    public boolean unregisterFeature(@NotNull FeatureId featureId) {
        final boolean present = isFeatureRegistered(featureId);
        if (present) REGISTERED_FEATURES.remove(featureId);
        return present;
    }

    public Optional<Feature> getFeature(@NotNull FeatureId featureId, @NotNull FeatureContext featureContext) {
        return Optional.ofNullable(REGISTERED_FEATURES.get(featureId))
                .map(featureFeatureFactory -> featureFeatureFactory.create(featureContext));
    }

    public boolean isFeatureRegistered(@NotNull FeatureId featureId) {
        return REGISTERED_FEATURES.containsKey(featureId);
    }

    @NotNull
    public Set<FeatureId> getAllFeatureIds() {
        return Set.copyOf(REGISTERED_FEATURES.keySet());
    }
}
