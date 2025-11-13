package app.lyrablock.lyra.mixin.client;

import app.lyrablock.lyra.event.ScreenHandlerEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
class ScreenHandlerMixin {
    @Inject(method = "clicked", at = @At("HEAD"))
    void lyra$onSlotClick(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci) {
        ScreenHandlerEvents.SLOT_CLICK.invoker().onSlotClick(slotIndex, button, actionType, player);
    }
}
