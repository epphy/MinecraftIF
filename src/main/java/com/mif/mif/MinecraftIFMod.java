package com.mif.mif;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mif.mif.command.ReloadCommand;
import com.mif.mif.config.ConfigManager;
import com.mif.mif.core.feature.FeatureManager;
import com.mif.mif.core.feature.FeatureRegistry;
import com.mif.mif.util.MIFLogger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.event.Level;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MinecraftIFMod implements ModInitializer {
    private static final int CURRENT_VERSION = 100;
    private static final String MOD_ID = "mif";

    @Override
    public void onInitialize() {
        setupLogger();
        initializeRegistry();
        initializeFeatures();
        initializeConfig();
        registerCommands();
        checkVersion();
        printWelcome();
    }

    private void setupLogger() {
        MIFLogger.setCurrentLevel(Level.DEBUG);
    }

    private void initializeRegistry() {
        FeatureRegistry.init();
    }

    private void initializeFeatures() {
        FeatureManager.init();
    }

    private void initializeConfig() {
        ConfigManager.init();
    }

    private void registerCommands() {
        ReloadCommand.register();
    }

    private void checkVersion() {
        final String latestVersion = getLatestVersion();
        if (isNewerVersion(latestVersion)) {
            MIFLogger.info(this, "New version has been found at:");
            MIFLogger.info(this, "https://github.com/epphy/MinecraftIF/releases/latest");
            return;
        }
        MIFLogger.info(this, "You are running latest version");
    }

    private boolean isNewerVersion(String latestVersion) {
        try {
            latestVersion = latestVersion.startsWith("v") ? latestVersion.substring(1) : latestVersion;
            final int latestVersionNumber = Integer.parseInt(latestVersion.replace(".", ""));
            return CURRENT_VERSION < latestVersionNumber;
        } catch (NumberFormatException e) {
            MIFLogger.error(this, "Error parsing latest version to number", e);
            return false;
        }
    }

    private String getLatestVersion() {
        try (final HttpClient httpClient = HttpClient.newHttpClient();) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/epphy/MinecraftIF/releases/latest"))
                    .header("Accept", "application/vnd.github.v3+json")
                    .build();

            final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return "";

            final JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            if (!json.has("tag_name")) return "";
            return json.get("tag_name").getAsString();
        } catch (IOException | InterruptedException e) {
            MIFLogger.error(this, "Error fetching latest version: %s".formatted(e.getMessage()), e);
            return "";
        }
    }

    private void printWelcome() {
        MIFLogger.announce("========================");
        MIFLogger.announce("Mod has been initialized");
        MIFLogger.announce("");
        MIFLogger.announce("To report issues or suggest something,");
        MIFLogger.announce("use the following link:");
        MIFLogger.announce("https://github.com/epphy/MinecraftIF");
        MIFLogger.announce("");
        MIFLogger.announce("Enjoy the game!");
        MIFLogger.announce("========================");
    }

    public static String getModId() {
        return MOD_ID;
    }

    public static boolean isServer() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }
}
