package name.lyrablock.mixin.client;

import name.lyrablock.event.CancellableEventResult;
import name.lyrablock.event.ItemUseEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    void lyra$use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        final var itemStack = (ItemStack) (Object) this;
        ItemUseEvents.USE.invoker().onUse(itemStack);
        final var result = ItemUseEvents.MODIFY_USE.invoker().onModifyUse(itemStack);
        if (result == CancellableEventResult.CANCEL) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
