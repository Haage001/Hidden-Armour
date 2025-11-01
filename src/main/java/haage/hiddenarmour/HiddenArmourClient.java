package haage.hiddenarmour;

import haage.hiddenarmour.client.HiddenArmourScreen;
import haage.hiddenarmour.config.HiddenArmourConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import org.lwjgl.glfw.GLFW;

public class HiddenArmourClient implements ClientModInitializer {
    private KeyBinding toggleArmorKey;
    private KeyBinding openGuiKey;

    private static final KeyBinding.Category HIDDEN_ARMOUR_CATEGORY = KeyBinding.Category
            .create(Identifier.of("hiddenarmour"));

    @Override
    public void onInitializeClient() {
        // Ensure config is loaded
        HiddenArmourConfig.get();

        // Toggle all armour with J
        toggleArmorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hiddenarmour.toggle",
                GLFW.GLFW_KEY_J,
                HIDDEN_ARMOUR_CATEGORY));

        // Open GUI with U
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hiddenarmour.openGui",
                GLFW.GLFW_KEY_U,
                HIDDEN_ARMOUR_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Toggle global hideArmour
            while (toggleArmorKey.wasPressed()) {
                HiddenArmourConfig cfg = HiddenArmourConfig.get();
                cfg.hideArmour = !cfg.hideArmour;
                HiddenArmourConfig.save();
                client.player.sendMessage(
                        Text.literal("Armour: ")
                                .append(Text.literal(cfg.hideArmour ? "Hidden" : "Shown")
                                        .formatted(cfg.hideArmour ? Formatting.GREEN : Formatting.RED)),
                        true);

            }

            // Open the settings screen
            while (openGuiKey.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();
                mc.setScreen(new HiddenArmourScreen(mc.currentScreen));
            }
        });
    }
}
