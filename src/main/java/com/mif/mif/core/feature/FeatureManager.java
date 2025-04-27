package com.mif.mif.core.feature;

import com.mif.mif.MinecraftIFMod;
import com.mif.mif.config.ConfigManager;
import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureManager {

    private static FeatureManager instance;
    private final Map<FeatureId, Feature> features = new HashMap<>();

    public static FeatureManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FeatureManager has not been initialized yet");
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new FeatureManager();
        }

        instance.loadFeaturesFromConfig();
        MIFLogger.info(instance, "initialized");
    }

    public void loadFeaturesFromConfig() {
        int loaded = 0;

        if (MinecraftIFMod.isServer()) {
            final Set<Map.Entry<FeatureId, Boolean>> featureEntries = ConfigManager.getInstance().getServerConfig().getEnabledFeatures().entrySet();

            for (final Map.Entry<FeatureId, Boolean> feature : featureEntries) {
                if (!feature.getValue()) continue;

                final FeatureId featureId = feature.getKey();
                final Optional<Feature> optionalFeature = FeatureRegistry.getInstance().getFeature(featureId);

                if (optionalFeature.isEmpty()) {
                    MIFLogger.warn(this, "Tried to enable an unknown feature: %s".formatted(featureId.name()));
                    continue;
                }

                if (!addFeature(featureId)) {
                    MIFLogger.warn(this, "Failed to enable a feature '%s' from config".formatted(featureId.name()));
                    continue;
                }

                loaded++;
            }

            MIFLogger.debug(this, "Successfully enabled %d/%d features from config in total".formatted(loaded, features.size()));
            return;
        }

        for (final Map.Entry<FeatureId, Boolean> feature : ConfigManager.getInstance().getClientConfig().getEnabledFeatures().entrySet()) {
            if (!feature.getValue()) continue;

            final FeatureId featureId = feature.getKey();
            final Optional<Feature> optionalFeature = FeatureRegistry.getInstance().getFeature(featureId);

            if (optionalFeature.isEmpty()) {
                MIFLogger.warn(this, "Tried to enable an unknown feature: %s".formatted(featureId.name()));
                continue;
            }

            if (!addFeature(featureId)) {
                MIFLogger.warn(this, "Failed to enable a feature '%s' from config".formatted(featureId.name()));
                continue;
            }

            loaded++;
        }

        MIFLogger.debug(this, "Successfully enabled %d/%d features from config in total".formatted(loaded, features.size()));
    }

    public boolean updateFeatureState(@NotNull FeatureId featureId, boolean enabled) {
        if (enabled) return addFeature(featureId);
        else return removeFeature(featureId);
    }

    public boolean addFeature(@NotNull FeatureId featureId) {
        final boolean absent = !isFeatureEnabled(featureId);
        if (!absent){
            MIFLogger.warn(this, "Could not enable feature '%s' because it is already enabled".formatted(featureId.name()));
            return false;
        }

        final Optional<Feature> optionalFeature = FeatureRegistry.getInstance().getFeature(featureId);
        if (optionalFeature.isEmpty()) {
            MIFLogger.debug(this, "Could not enable feature '%s' because its supplier is corrupted".formatted(featureId.name()));
            return false;
        }

        optionalFeature.get().enable();
        features.put(featureId, optionalFeature.get());
        MIFLogger.debug(this, "Enabled feature: %s".formatted(featureId.name()));
        return true;
    }

    public boolean removeFeature(@NotNull FeatureId featureId) {
        final boolean present = isFeatureEnabled(featureId);
        if (!present) {
            MIFLogger.warn(this, "Could not disable feature '%s' because it is not enabled".formatted(featureId.name()));
            return false;
        }

        features.get(featureId).disable();
        features.remove(featureId);
        MIFLogger.debug(this, "Disabled feature: %s".formatted(featureId.name()));
        return true;
    }

    public boolean isFeatureEnabled(@NotNull FeatureId featureId) {
        return features.containsKey(featureId);
    }
}
