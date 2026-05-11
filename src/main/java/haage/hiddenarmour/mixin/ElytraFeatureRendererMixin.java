package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import haage.hiddenarmour.config.HiddenArmourConfig;
import haage.hiddenarmour.render.ArmourOpacityContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@Mixin(WingsLayer.class)
public class ElytraFeatureRendererMixin {
        @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;"
                        + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
                        + "ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;"
                        + "FF)V", at = @At("HEAD"), cancellable = true)
        private void onRenderElytra(
                        PoseStack poseStack,
                        SubmitNodeCollector submitNodeCollector,
                        int light,
                        HumanoidRenderState state,
                        float limbAngle,
                        float limbDistance,
                        CallbackInfo ci) {
                if (HiddenArmourConfig.get().hideArmour
                                && HiddenArmourConfig.get().includeElytra
                                && state instanceof AvatarRenderState avatarState) {
                        var localPlayer = Minecraft.getInstance().player;
                        boolean isLocal = localPlayer != null && avatarState.id == localPlayer.getId();
                        if (!HiddenArmourConfig.get().scopeAppliesToLocalPlayer(isLocal)) return;
                        int alpha = HiddenArmourConfig.get().getArmourOpacityAlpha();
                        if (alpha <= 0) {
                                ci.cancel();
                                return;
                        }
                        ArmourOpacityContext.setAlpha(alpha);
                }
        }

        @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;"
                        + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
                        + "ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;"
                        + "FF)V", at = @At("RETURN"))
        private void onRenderElytraReturn(
                        PoseStack poseStack,
                        SubmitNodeCollector submitNodeCollector,
                        int light,
                        HumanoidRenderState state,
                        float limbAngle,
                        float limbDistance,
                        CallbackInfo ci) {
                ArmourOpacityContext.clear();
        }
}
