package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(
            method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;"
                    + "Lnet/minecraft/text/Text;"
                    + "Lnet/minecraft/client/util/math/MatrixStack;"
                    + "Lnet/minecraft/client/render/VertexConsumerProvider;"
                    + "I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderLabelIfPresent(
            EntityRenderState state,     // raw EntityRenderState
            Text text,
            MatrixStack matrices,
            VertexConsumerProvider provider,
            int light,
            CallbackInfo ci
    ) {
        if (HiddenArmourConfig.get().hideNameTags) {
            ci.cancel();
        }
    }
}
