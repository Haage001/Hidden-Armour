package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.WolfArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(WolfArmorFeatureRenderer.class)
public class WolfArmorFeatureRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
            + "ILnet/minecraft/client/render/entity/state/WolfEntityRenderState;"
            + "FF)V", at = @At("HEAD"), cancellable = true)
    private void onRenderWolfArmor(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            WolfEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        if (HiddenArmourConfig.get().hideWolfArmor) {
            ci.cancel();
        }
    }
}
