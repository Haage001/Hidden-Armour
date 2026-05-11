package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import haage.hiddenarmour.config.HiddenArmourConfig;
import haage.hiddenarmour.render.ShieldOpacityContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mixin(ItemInHandLayer.class)
public class ShieldFeatureRendererMixin {
    @Inject(method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;"
                    + "Lnet/minecraft/client/renderer/item/ItemStackRenderState;"
                    + "Lnet/minecraft/world/item/ItemStack;"
                    + "Lnet/minecraft/world/entity/HumanoidArm;"
                    + "Lcom/mojang/blaze3d/vertex/PoseStack;"
                    + "Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"), cancellable = true)
    private void hideShield(
                    ArmedEntityRenderState state,
                    ItemStackRenderState item,
                    ItemStack itemStack,
                    HumanoidArm arm,
                    PoseStack poseStack,
                    SubmitNodeCollector submitNodeCollector,
                    int light,
                    CallbackInfo ci) {
        ShieldOpacityContext.clear();

        // First-person hand rendering does not go through this layer, so this only affects entity renders.
        boolean shieldActiveOnThisArm = state.ticksUsingItem(arm) > 0.0F;
        if (!shouldAffectShieldInHideMode(state, itemStack, shieldActiveOnThisArm)) {
            return;
        }

        int alpha = HiddenArmourConfig.get().getArmourOpacityAlpha();
        if (alpha <= 0) {
            ci.cancel();
            return;
        }

        ShieldOpacityContext.activate(alpha);
    }

    @Inject(method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;"
                    + "Lnet/minecraft/client/renderer/item/ItemStackRenderState;"
                    + "Lnet/minecraft/world/item/ItemStack;"
                    + "Lnet/minecraft/world/entity/HumanoidArm;"
                    + "Lcom/mojang/blaze3d/vertex/PoseStack;"
                    + "Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("RETURN"))
    private void clearShieldOpacityState(
                    ArmedEntityRenderState state,
                    ItemStackRenderState item,
                    ItemStack itemStack,
                    HumanoidArm arm,
                    PoseStack poseStack,
                    SubmitNodeCollector submitNodeCollector,
                    int light,
                    CallbackInfo ci) {
        ShieldOpacityContext.clear();
    }

    private static boolean shouldAffectShieldInHideMode(
                    ArmedEntityRenderState state,
                    ItemStack itemStack,
                    boolean shieldActiveOnThisArm) {
        if (!HiddenArmourConfig.get().hideArmour
                || !HiddenArmourConfig.get().hideShield
                || !(state instanceof AvatarRenderState avatarState)
                || !itemStack.is(Items.SHIELD)
                || shieldActiveOnThisArm) {
            return false;
        }
        var localPlayer = Minecraft.getInstance().player;
        boolean isLocal = localPlayer != null && avatarState.id == localPlayer.getId();
        return HiddenArmourConfig.get().scopeAppliesToLocalPlayer(isLocal);
    }
}
