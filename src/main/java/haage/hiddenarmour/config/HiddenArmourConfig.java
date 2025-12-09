package haage.hiddenarmour.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public class HiddenArmourConfig {
    // Path to the config file: <minecraft>/config/hiddenarmour.json
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("hiddenarmour.json");

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // Global toggles
    public boolean hideArmour            = false;
    public boolean includeElytra         = false;
    public boolean hideHorseArmor        = false;
    public boolean hideWolfArmor         = false;
    public boolean hideNautilusArmor     = false;
    public boolean hideEnchantmentGlint  = false;
    public boolean hideNameTags          = false;

    // Per-piece toggles
    public boolean showHelmet     = false;
    public boolean showChestplate = false;
    public boolean showLeggings   = false;
    public boolean showBoots      = false;

    // Singleton instance
    private static HiddenArmourConfig instance;

    /** Get (and lazy-load) the singleton config. */
    public static HiddenArmourConfig get() {
        if (instance == null) load();
        return instance;
    }

    /** Load from disk or create defaults if missing. */
    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    instance = GSON.fromJson(reader, HiddenArmourConfig.class);
                }
            } else {
                instance = new HiddenArmourConfig();
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
            instance = new HiddenArmourConfig();
        }
    }

    /** Write current settings out to disk. */
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(get(), writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---- Per-piece getters & setters ----

    public boolean isShowHelmet() {
        return showHelmet;
    }
    public void setShowHelmet(boolean showHelmet) {
        this.showHelmet = showHelmet;
    }

    public boolean isShowChestplate() {
        return showChestplate;
    }
    public void setShowChestplate(boolean showChestplate) {
        this.showChestplate = showChestplate;
    }

    public boolean isShowLeggings() {
        return showLeggings;
    }
    public void setShowLeggings(boolean showLeggings) {
        this.showLeggings = showLeggings;
    }

    public boolean isShowBoots() {
        return showBoots;
    }
    public void setShowBoots(boolean showBoots) {
        this.showBoots = showBoots;
    }
}
