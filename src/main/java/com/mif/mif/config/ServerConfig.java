package com.mif.mif.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mif.mif.core.feature.FeatureId;
import com.mif.mif.core.feature.FeatureManager;
import com.mif.mif.core.feature.FeatureRegistry;
import com.mif.mif.util.MIFLogger;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public final class ServerConfig implements Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final TypeToken<Map<FeatureId, Boolean>> TYPE_TOKEN = new TypeToken<>(){};
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("mif_server_config.json");
    private final Map<FeatureId, Boolean> enabledFeatures = new EnumMap<>(FeatureId.class);
    private File file;

    public void init() {
        initFile();
        load();
        MIFLogger.debug(this, "Loaded config with following parameters: %s".formatted(enabledFeatures));
    }

    private void initFile() {
        if (file == null) {
            file = CONFIG_PATH.toFile();
        }

        if (!file.exists()) {
            upToDateData();
        }
    }

    public void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(GSON.toJson(enabledFeatures));
        } catch (IOException e) {
            throw new RuntimeException("ServerConfig failed to write to config", e);
        }
    }

    public void load() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final Map<FeatureId, Boolean> tempData = GSON.fromJson(reader, TYPE_TOKEN);
            if (tempData == null) return;
            enabledFeatures.clear();
            for (final Map.Entry<FeatureId, Boolean> entry : tempData.entrySet()) {
                setFeatureEnabled(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException("ServerConfig failed to read config", e);
        }
    }

    private void upToDateData() {
        for (final FeatureId featureId : FeatureRegistry.getInstance().getAllServerFeatureIds()) {
            enabledFeatures.putIfAbsent(featureId, false);
        }
        save();
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
