package app.lyrablock.lyra.mixin.client;

import app.lyrablock.lyra.event.KeyBindingOverrideCallback;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyBindingMixin {
    @Shadow
    @Final
    private String name;

    @Inject(method = "isDown", at = @At("HEAD"), cancellable = true)
    void lyra$isPressed(CallbackInfoReturnable<Boolean> cir) {
        final var result = KeyBindingOverrideCallback.EVENT.invoker().override(name);
        if (result != null) cir.setReturnValue(result);
    }
}
