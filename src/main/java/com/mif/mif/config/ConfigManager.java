package com.mif.mif.config;

import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigManager {
    private static ConfigManager instance;
    private Config config;

    public static ConfigManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized yet");
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new ConfigManager();
        }

        instance.config = new Config();
        instance.config.init();

        MIFLogger.info(instance, "initialized");
    }

}
