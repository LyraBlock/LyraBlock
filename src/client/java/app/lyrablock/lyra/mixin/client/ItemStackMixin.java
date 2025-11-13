package app.lyrablock.lyra.mixin.client;

import app.lyrablock.lyra.event.CancellableEventResult;
import app.lyrablock.lyra.event.ItemUseEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    void lyra$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        final var itemStack = (ItemStack) (Object) this;
        ItemUseEvents.USE.invoker().onUse(itemStack);
        final var result = ItemUseEvents.MODIFY_USE.invoker().onModifyUse(itemStack);
        if (result == CancellableEventResult.CANCEL) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
