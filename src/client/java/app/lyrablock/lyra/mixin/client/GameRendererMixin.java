package app.lyrablock.lyra.mixin.client;

import app.lyrablock.lyra.event.FovEvents;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(
        method = "getFov",
        at = @At("RETURN")
    )
    private float lyra$getFov(float fov, @Local(argsOnly = true) float tickDelta) {
        final var multiplier = FovEvents.MULTIPLY.invoker().multiply(tickDelta);
        return fov * multiplier;
    }
}
