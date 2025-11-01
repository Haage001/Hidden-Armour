package haage.hiddenarmour;

import static net.minecraft.server.command.CommandManager.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

public class Hiddenarmour implements ModInitializer {
	public static final String MOD_ID = "hiddenarmour";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// STEP 2: Load the JSON config (or create it if missing)
		HiddenArmourConfig.get();

		LOGGER.info("Hello Fabric world! xD");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					literal("hiddenarmour")
							// — armour toggle commands —
							.then(literal("enable")
									.executes(ctx -> {
										HiddenArmourConfig.get().hideArmour = true;
										HiddenArmourConfig.save(); // persist change
										ctx.getSource().sendFeedback(
												() -> Text.literal("§aHidden armour enabled."),
												false);
										return 1;
									}))
							.then(literal("disable")
									.executes(ctx -> {
										HiddenArmourConfig.get().hideArmour = false;
										HiddenArmourConfig.save(); // persist change
										ctx.getSource().sendFeedback(
												() -> Text.literal("§cHidden armour disabled."),
												false);
										return 1;
									}))
							.then(literal("status")
									.executes(ctx -> {
										boolean enabled = HiddenArmourConfig.get().hideArmour;
										ctx.getSource().sendFeedback(
												() -> Text.literal(
														"Hidden armour is " + (enabled ? "§aENABLED" : "§cDISABLED")),
												false);
										return enabled ? 1 : 0;
									}))
							// — elytra inclusion commands —
							.then(literal("elytra")
									.then(literal("enable")
											.executes(ctx -> {
												HiddenArmourConfig.get().includeElytra = true;
												HiddenArmourConfig.save(); // persist change
												ctx.getSource().sendFeedback(
														() -> Text.literal(
																"§aElytra will now be hidden along with armour."),
														false);
												return 1;
											}))
									.then(literal("disable")
											.executes(ctx -> {
												HiddenArmourConfig.get().includeElytra = false;
												HiddenArmourConfig.save(); // persist change
												ctx.getSource().sendFeedback(
														() -> Text.literal(
																"§cElytra will now be shown even when armour is hidden."),
														false);
												return 1;
											}))
									.then(literal("status")
											.executes(ctx -> {
												boolean inc = HiddenArmourConfig.get().includeElytra;
												ctx.getSource().sendFeedback(
														() -> Text.literal(
																"Elytra hiding is "
																		+ (inc ? "§aENABLED" : "§cDISABLED")),
														false);
												return inc ? 1 : 0;
											}))));
		});
	}
}
