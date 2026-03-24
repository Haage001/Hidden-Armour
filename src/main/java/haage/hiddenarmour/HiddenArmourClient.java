package haage.hiddenarmour;

import haage.hiddenarmour.client.HiddenArmourScreen;
import haage.hiddenarmour.config.HiddenArmourConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

public class HiddenArmourClient implements ClientModInitializer {
    private KeyMapping toggleArmorKey;
    private KeyMapping openGuiKey;

    private static final KeyMapping.Category HIDDEN_ARMOUR_CATEGORY = KeyMapping.Category
            .register(Identifier.fromNamespaceAndPath("hiddenarmour", "controls"));

    @Override
    public void onInitializeClient() {
        // Ensure config is loaded
        HiddenArmourConfig.get();

        // Toggle all armour with J
        toggleArmorKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.hiddenarmour.toggle",
                GLFW.GLFW_KEY_J,
                HIDDEN_ARMOUR_CATEGORY));

        // Open GUI with U
        openGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.hiddenarmour.openGui",
                GLFW.GLFW_KEY_U,
                HIDDEN_ARMOUR_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Toggle global hideArmour
            while (toggleArmorKey.consumeClick()) {
                HiddenArmourConfig cfg = HiddenArmourConfig.get();
                cfg.hideArmour = !cfg.hideArmour;
                HiddenArmourConfig.save();
                if (client.player != null) {
                    client.player.sendOverlayMessage(
                            Component.literal("Armour: ")
                                    .append(Component.literal(cfg.hideArmour ? "Hidden" : "Shown")
                                            .withStyle(cfg.hideArmour ? ChatFormatting.GREEN : ChatFormatting.RED)));
                }

            }

            // Open the settings screen
            while (openGuiKey.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.setScreen(new HiddenArmourScreen(minecraft.screen));
            }
        });
    }
}
