package com.mif.mif.config;

import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigManager {

    private static ConfigManager instance;
    private ServerConfig serverConfig;
    private ClientConfig clientConfig;

    public static ConfigManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConfigManager has not been initialized yet");
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new ConfigManager();
        }

        instance.loadCertainConfigs();
        MIFLogger.info(instance, "ConfigManager initialized");
    }

    private void loadCertainConfigs() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            MIFLogger.info(this, "Loading client config...");
            clientConfig = new ClientConfig();
            clientConfig.init();
            return;
        }
        MIFLogger.info(this, "Loading server config...");
        serverConfig = new ServerConfig();
        serverConfig.init();
    }
}
