package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;

@Mixin(WolfArmorLayer.class)
public class WolfArmorFeatureRendererMixin {
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;"
            + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
            + "ILnet/minecraft/client/renderer/entity/state/WolfRenderState;"
            + "FF)V", at = @At("HEAD"), cancellable = true)
    private void onRenderWolfArmor(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int light,
            WolfRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        if (HiddenArmourConfig.get().hideWolfArmor) {
            ci.cancel();
        }
    }
}
