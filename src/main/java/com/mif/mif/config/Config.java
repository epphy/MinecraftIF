package com.mif.mif.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mif.mif.core.feature.FeatureContext;
import com.mif.mif.core.feature.FeatureId;
import com.mif.mif.core.feature.FeatureManager;
import com.mif.mif.core.feature.FeatureRegistry;
import com.mif.mif.util.MIFLogger;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Config {
    private static final Gson GSON = new Gson();
    private static final TypeToken<Map<FeatureId, Boolean>> TYPE_TOKEN = new TypeToken<>(){};
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("mif_config.json");
    private final Map<FeatureId, Boolean> enabledFeatures = new HashMap<>();
    private File file;

    void init() {
        initFile();
        load();
    }

    private void initFile() {
        if (file == null) file = CONFIG_PATH.toFile();
    }

    public void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(GSON.toJson(enabledFeatures));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to config", e);
        }
    }

    public void load() {
        if (file.exists()) {
            try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                final Map<FeatureId, Boolean> tempData = GSON.fromJson(reader, TYPE_TOKEN);
                if (tempData == null) return;
                enabledFeatures.clear();
                enabledFeatures.putAll(tempData);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read config", e);
            }
        }

        upToDateFeatures();
    }

    private void upToDateFeatures() {
        for (final FeatureId featureId : FeatureRegistry.getInstance().getAllFeatureIds()) {
            enabledFeatures.putIfAbsent(featureId, false);
        }
    }

    // Todo; more graceful approach to saving
    public void setFeatureEnabled(@NotNull FeatureId featureId, boolean enabled) {
        MIFLogger.info(this, "Config update. Feature id: %s; enabled: %b".formatted(featureId, enabled));
        enabledFeatures.put(featureId, enabled);
        syncWithFeatureManager(featureId);
    }

    public boolean isFeatureEnabled(@NotNull FeatureId featureId) {
        return enabledFeatures.getOrDefault(featureId, false);
    }

    // Todo; more graceful approach to providing context
    public void syncWithFeatureManager(@NotNull FeatureId featureId) {
        if (isFeatureEnabled(featureId)) FeatureManager.getInstance().addFeature(featureId, new FeatureContext());
        else FeatureManager.getInstance().removeFeature(featureId);
    }

    public Map<FeatureId, Boolean> getEnabledFeatures() {
        return Map.copyOf(enabledFeatures);
    }

    public Set<FeatureId> getFeatureIds() {
        return Set.copyOf(enabledFeatures.keySet());
    }
}
