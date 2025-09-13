package name.lyrablock.mixin.client;

import name.lyrablock.event.ScreenHandlerEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
class ScreenHandlerMixin {
    @Inject(method = "onSlotClick", at = @At("HEAD"))
    void lyra$onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        ScreenHandlerEvents.SLOT_CLICK.invoker().onSlotClick(slotIndex, button, actionType, player);
    }
}
