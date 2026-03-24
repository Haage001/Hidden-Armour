package haage.hiddenarmour.mixin;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.NautilusRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;

@Mixin(SimpleEquipmentLayer.class)
public abstract class SimpleEquipmentLayerMixin<S extends LivingEntityRenderState> {
    @Shadow
    @Final
    private EquipmentClientInfo.LayerType layer;

    @Shadow
    @Final
    private Function<S, ItemStack> itemGetter;

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;"
            + "Lnet/minecraft/client/renderer/SubmitNodeCollector;"
            + "ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;"
            + "FF)V", at = @At("HEAD"), cancellable = true)
    private void hiddenArmour$onSubmit(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int light,
            S state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci) {
        ItemStack stack = itemGetter.apply(state);
        if (stack.isEmpty()) {
            return;
        }

        HiddenArmourConfig config = HiddenArmourConfig.get();
        if (state instanceof EquineRenderState && isHorseLayer() && config.hideHorseArmor) {
            ci.cancel();
            return;
        }

        if (state instanceof NautilusRenderState && isNautilusLayer() && config.hideNautilusArmor) {
            ci.cancel();
        }
    }

    private boolean isHorseLayer() {
        return layer == EquipmentClientInfo.LayerType.HORSE_BODY
                || layer == EquipmentClientInfo.LayerType.LLAMA_BODY
                || layer == EquipmentClientInfo.LayerType.HORSE_SADDLE
                || layer == EquipmentClientInfo.LayerType.DONKEY_SADDLE
                || layer == EquipmentClientInfo.LayerType.MULE_SADDLE
                || layer == EquipmentClientInfo.LayerType.ZOMBIE_HORSE_SADDLE
                || layer == EquipmentClientInfo.LayerType.SKELETON_HORSE_SADDLE;
    }

    private boolean isNautilusLayer() {
        return layer == EquipmentClientInfo.LayerType.NAUTILUS_BODY
                || layer == EquipmentClientInfo.LayerType.NAUTILUS_SADDLE;
    }
}