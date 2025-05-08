package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks ItemRenderer#getItemGlintConsumer(...) and, if hideEnchantmentGlint is true,
 * returns the *same* layer instead of the glint layer—thus no glint effect.
 */
@Mixin(net.minecraft.client.render.item.ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(
            method = "getItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;"
                    + "Lnet/minecraft/client/render/RenderLayer;ZZ)"
                    + "Lnet/minecraft/client/render/VertexConsumer;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetItemGlintConsumer(
            VertexConsumerProvider vcp,
            RenderLayer layer,
            boolean direct,
            boolean glint,
            CallbackInfoReturnable<VertexConsumer> cir
    ) {
        if (HiddenArmourConfig.get().hideEnchantmentGlint) {
            // simply return the *item’s* normal layer, no glint
            cir.setReturnValue(vcp.getBuffer(layer));
        }
    }
}
