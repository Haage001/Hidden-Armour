package haage.hiddenarmour.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;

import haage.hiddenarmour.render.ShieldOpacityContext;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;

@Mixin(ShieldSpecialRenderer.class)
public abstract class ShieldSpecialRendererMixin {
    @Redirect(method = "submit(Lnet/minecraft/core/component/DataComponentMap;"
            + "Lcom/mojang/blaze3d/vertex/PoseStack;"
            + "Lnet/minecraft/client/renderer/SubmitNodeCollector;IIZI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void hiddenArmour$redirectShieldBaseModelSubmit(
            SubmitNodeCollector collector,
        Model<Object> model,
            Object state,
            PoseStack poseStack,
            int light,
            int overlay,
            int color,
            SpriteId spriteId,
            SpriteGetter spriteGetter,
            int seed,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        if (!ShieldOpacityContext.isActive() || ShieldOpacityContext.getAlpha() >= 255) {
            collector.submitModel(model, state, poseStack, light, overlay, color, spriteId, spriteGetter, seed, crumblingOverlay);
            return;
        }

        int alphaColor = (ShieldOpacityContext.getAlpha() << 24) | 0x00FFFFFF;
        TextureAtlasSprite sprite = spriteGetter.get(spriteId);
        collector.submitModel(
                model,
                state,
                poseStack,
                spriteId.renderType(RenderTypes::entityTranslucent),
                light,
                overlay,
                alphaColor,
                sprite,
                seed,
                crumblingOverlay);
    }
}
