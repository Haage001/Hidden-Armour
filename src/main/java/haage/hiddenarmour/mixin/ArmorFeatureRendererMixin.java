package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Unique
    private boolean hiddenArmour_applyToggles = false;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
            + "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;"
            + "FF)V", at = @At("HEAD"))
    private void beforeRender(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            BipedEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        hiddenArmour_applyToggles = HiddenArmourConfig.get().hideArmour
                && state instanceof PlayerEntityRenderState;
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
            + "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;"
            + "FF)V", at = @At("TAIL"))
    private void afterRender(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            BipedEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        hiddenArmour_applyToggles = false;
    }

    @Inject(method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;"
            + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
            + "Lnet/minecraft/item/ItemStack;"
            + "Lnet/minecraft/entity/EquipmentSlot;"
            + "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderArmor(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
            BipedEntityRenderState state,
            CallbackInfo ci) {
        if (!hiddenArmour_applyToggles)
            return;

        var cfg = HiddenArmourConfig.get();

        switch (slot) {
            case HEAD:
                if (!cfg.isShowHelmet())
                    ci.cancel();
                break;
            case CHEST:
                if (!cfg.isShowChestplate())
                    ci.cancel();
                break;
            case LEGS:
                if (!cfg.isShowLeggings())
                    ci.cancel();
                break;
            case FEET:
                if (!cfg.isShowBoots())
                    ci.cancel();
                break;
            default:
                // no other slots
        }
    }
}
