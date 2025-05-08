package haage.hiddenarmour.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class HiddenArmourConfig {
    // Path to the config file: <minecraft>/config/hiddenarmour.json
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("hiddenarmour.json");

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // The settings to persist
    public boolean hideArmour = false;
    public boolean includeElytra = false;
    public boolean hideHorseArmor = false;
    public boolean hideEnchantmentGlint = false;
    public boolean hideNameTags        = false;

    // Singleton instance
    private static HiddenArmourConfig instance;

    /**
     * Gets the singleton, loading from disk if necessary.
     */
    public static HiddenArmourConfig get() {
        if (instance == null) load();
        return instance;
    }

    /**
     * Loads the config from file or creates defaults if missing.
     */
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

    /**
     * Saves the current config to disk.
     */
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
}
