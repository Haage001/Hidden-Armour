package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true)
    private void onHasLabel(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (HiddenArmourConfig.get().hideNameTags) {
            cir.setReturnValue(false);
        }
    }
}
