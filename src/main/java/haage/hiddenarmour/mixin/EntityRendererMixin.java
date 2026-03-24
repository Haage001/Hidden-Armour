package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
    private void onShouldShowName(Entity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (HiddenArmourConfig.get().hideNameTags) {
            cir.setReturnValue(false);
        }
    }
}
