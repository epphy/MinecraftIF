package com.mif.mif.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mif.mif.core.feature.FeatureId;
import com.mif.mif.core.feature.FeatureManager;
import com.mif.mif.core.feature.FeatureRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public final class ClientConfig implements Config {

    private static final Gson GSON = new Gson();
    private static final TypeToken<Map<FeatureId, Boolean>> TYPE_TOKEN = new TypeToken<>(){};
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("mif_client_config.json");
    private final Map<FeatureId, Boolean> enabledFeatures = new EnumMap<>(FeatureId.class);
    private File file;

    public void init() {
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

        upToDateData();
    }

    public void upToDateData() {
        for (final FeatureId featureId : FeatureRegistry.getInstance().getAllFeatureIds()) {
            enabledFeatures.putIfAbsent(featureId, false);
        }
    }

    public void setFeatureEnabled(@NotNull FeatureId featureId, boolean enabled) {
        enabledFeatures.put(featureId, enabled);
        syncWithFeatureManager(featureId, enabled);
    }

    // Todo; make sure to handle gracefully
    private void syncWithFeatureManager(@NotNull FeatureId featureId, boolean enabled) {
        FeatureManager.getInstance().updateFeatureState(featureId, enabled);
    }

    public boolean isFeatureEnabled(@NotNull FeatureId featureId) {
        return enabledFeatures.getOrDefault(featureId, false);
    }

    public Map<FeatureId, Boolean> getEnabledFeatures() {
        return Map.copyOf(enabledFeatures);
    }

}
