package com.mif.mif.config;

import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigManager {
    private static ConfigManager instance;
    private ServerConfig serverConfig;

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

        instance.serverConfig = new ServerConfig();
        instance.serverConfig.init();

        MIFLogger.info(instance, "initialized");
    }

}
