package com.mif.mif.core.feature;

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
        for (final Map.Entry<FeatureId, Boolean> feature : ConfigManager.getInstance().getServerConfig().getEnabledFeatures().entrySet()) {
            if (feature.getValue()) addFeature(feature.getKey());
        }
    }

    public boolean updateFeatureState(@NotNull FeatureId featureId, boolean enabled) {
        if (enabled) return addFeature(featureId);
        else return removeFeature(featureId);
    }

    public boolean addFeature(@NotNull FeatureId featureId) {
        final boolean absent = !isFeatureEnabled(featureId);
        if (!absent) return false;

        final Optional<Feature> optionalFeature = FeatureRegistry.getInstance().getFeature(featureId);
        if (optionalFeature.isEmpty()) return false;

        optionalFeature.get().enable();
        features.put(featureId, optionalFeature.get());
        return true;
    }

    public boolean removeFeature(@NotNull FeatureId featureId) {
        final boolean present = isFeatureEnabled(featureId);
        if (!present) return false;

        features.get(featureId).disable();
        features.remove(featureId);
        return true;
    }

    public boolean isFeatureEnabled(@NotNull FeatureId featureId) {
        return features.containsKey(featureId);
    }
}
