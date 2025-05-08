package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks the per-entity render(...) method:
 *   render(MatrixStack, VertexConsumerProvider, int,
 *          BipedEntityRenderState, float limbAngle, float limbDistance)
 * and only cancels when that state is a player.
 */
@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
                    + "Lnet/minecraft/client/render/VertexConsumerProvider;"
                    + "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;"
                    + "FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderArmor(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            BipedEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci
    ) {
        // only cancel armor for actual players
        if (HiddenArmourConfig.get().hideArmour
                && state instanceof PlayerEntityRenderState) {
            ci.cancel();
        }
    }
}
