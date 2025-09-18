package name.lyrablock.mixin.client;


import name.lyrablock.event.HandledScreenEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;

@Mixin(HandledScreen.class)
@Environment(EnvType.CLIENT)
public class HandledScreenMixin {
    @Shadow
    protected Slot focusedSlot;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Redirect(method = "drawMouseoverTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/util/Identifier;)V"))
    void lyra$drawTooltip(DrawContext instance, TextRenderer textRenderer, List<Text> text, Optional<TooltipData> data, int x, int y, @Nullable Identifier texture) {
        final var result = HandledScreenEvents.MODIFY_ITEM_TOOLTIP.invoker().onDrawItemTooltip(instance, focusedSlot, textRenderer, text, data.orElse(null), x, y, texture);

        if (!result.isCancel()) {
            instance.drawTooltip(textRenderer, text, data, x, y, texture);
        }
    }
}
