package name.lyrablock.mixin.client;

import name.lyrablock.event.DrawEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(
        method = "drawItemTooltip",
        at = @At("HEAD"),
        cancellable = true
    )
    void lyra$drawContext(TextRenderer textRenderer, ItemStack stack, int x, int y, CallbackInfo ci) {
        final var result = DrawEvents.MODIFY_ITEM_TOOLTIP.invoker().onDrawTooltip(textRenderer, stack, x, y);
        if (result.isCancel()) {
            ci.cancel();
        }
    }
}
