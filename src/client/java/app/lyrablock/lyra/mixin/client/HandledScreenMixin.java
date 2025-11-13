package app.lyrablock.lyra.mixin.client;


import app.lyrablock.lyra.event.HandledScreenEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
@Environment(EnvType.CLIENT)
public class HandledScreenMixin {
    @Shadow
    protected Slot hoveredSlot;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Redirect(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;setTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/resources/ResourceLocation;)V"))
    void lyra$drawTooltip(GuiGraphics instance, Font textRenderer, List<Component> text, Optional<TooltipComponent> data, int x, int y, @Nullable ResourceLocation texture) {
        final var result = HandledScreenEvents.MODIFY_ITEM_TOOLTIP.invoker().onDrawItemTooltip(instance, hoveredSlot, textRenderer, text, data.orElse(null), x, y, texture);

        if (!result.isCancel()) {
            instance.setTooltipForNextFrame(textRenderer, text, data, x, y, texture);
        }
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    void lyra$mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        HandledScreenEvents.MOUSE_SCROLLED.invoker().onMouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
