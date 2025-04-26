package com.mif.mif.client.config;

import com.mif.mif.config.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreenBuilder(ConfigManager.getInstance().getConfig()).createConfigScreen(parent);
    }

}
