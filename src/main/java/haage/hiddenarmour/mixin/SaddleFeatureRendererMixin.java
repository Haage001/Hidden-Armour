package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(SaddleFeatureRenderer.class)
public class SaddleFeatureRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
            + "ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;"
            + "FF)V", at = @At("HEAD"), cancellable = true)
    private void onRenderSaddleArmor(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            LivingEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        if (HiddenArmourConfig.get().hideHorseArmor) {
            ci.cancel();
        }
    }
}
