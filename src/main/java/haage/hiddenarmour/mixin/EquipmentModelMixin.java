package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public class EquipmentModelMixin {
    @Inject(method = "hasGlint", at = @At("HEAD"), cancellable = true)
    private void disableGlint(CallbackInfoReturnable<Boolean> cir) {
        if (HiddenArmourConfig.get().hideEnchantmentGlint) {
            cir.setReturnValue(false);
        }
    }
}
