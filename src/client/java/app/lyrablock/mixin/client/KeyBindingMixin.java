package app.lyrablock.mixin.client;

import app.lyrablock.event.KeyBindingOverrideCallback;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    @Shadow
    @Final
    private String translationKey;

    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    void lyra$isPressed(CallbackInfoReturnable<Boolean> cir) {
        final var result = KeyBindingOverrideCallback.EVENT.invoker().override(translationKey);
        if (result != null) cir.setReturnValue(result);
    }
}
