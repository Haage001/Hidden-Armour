package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import haage.hiddenarmour.render.ArmourOpacityContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class ArmorFeatureRendererMixin {
    @Unique
    private boolean hiddenArmour_applyToggles = false;

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;"
        + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
        + "ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;"
            + "FF)V", at = @At("HEAD"))
    private void beforeRender(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
            int light,
        HumanoidRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        ArmourOpacityContext.clear();
        if (HiddenArmourConfig.get().hideArmour && state instanceof AvatarRenderState avatarState) {
            var localPlayer = Minecraft.getInstance().player;
            boolean isLocal = localPlayer != null && avatarState.id == localPlayer.getId();
            hiddenArmour_applyToggles = HiddenArmourConfig.get().scopeAppliesToLocalPlayer(isLocal);
        } else {
            hiddenArmour_applyToggles = false;
        }
    }

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;"
        + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
        + "ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;"
            + "FF)V", at = @At("TAIL"))
    private void afterRender(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
            int light,
        HumanoidRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        hiddenArmour_applyToggles = false;
        ArmourOpacityContext.clear();
    }

    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;"
        + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
        + "Lnet/minecraft/world/item/ItemStack;"
        + "Lnet/minecraft/world/entity/EquipmentSlot;"
        + "ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderArmor(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
        HumanoidRenderState state,
            CallbackInfo ci) {
        if (!hiddenArmour_applyToggles)
            return;

        var cfg = HiddenArmourConfig.get();

        // In hide mode, the slider controls opacity percentage (0=vanilla, 100=fully hidden).
        int transparencyPercent = cfg.getArmourOpacityPercent();
        int alpha = Math.round(((100 - transparencyPercent) / 100.0F) * 255.0F);
        if (alpha <= 0) {
            ci.cancel();
            return;
        }
        ArmourOpacityContext.setAlpha(alpha);
    }

    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;"
        + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
        + "Lnet/minecraft/world/item/ItemStack;"
        + "Lnet/minecraft/world/entity/EquipmentSlot;"
        + "ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("RETURN"))
    private void onRenderArmorReturn(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
        HumanoidRenderState state,
            CallbackInfo ci) {
        ArmourOpacityContext.clear();
    }
}
