package com.mif.mif;

import com.mif.mif.util.MIFLogger;
import net.fabricmc.api.ModInitializer;

public class MinecraftIFMod implements ModInitializer {
    private static final String MOD_ID = "mif";

    @Override
    public void onInitialize() {
        printWelcome();
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
}
