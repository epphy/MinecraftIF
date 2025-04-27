package com.mif.mif.core.feature;

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

        MIFLogger.info(instance, "initialized");
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
