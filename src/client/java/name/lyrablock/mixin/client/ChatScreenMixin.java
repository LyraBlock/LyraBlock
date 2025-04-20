package name.lyrablock.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import name.lyrablock.event.ClickChatEventFactory;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
//    @Shadow protected TextFieldWidget chatField;


    @Inject(method = "mouseClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ChatScreen;handleTextClick(Lnet/minecraft/text/Style;)Z"
            )
    )
    void lyra$mouseClicked(
            double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir, @Local ChatHud chatHud
    ) {
        ClickChatEventFactory.INSTANCE.dispatch(new ClickChatEventFactory.ClickChatEvent(
                mouseX, mouseY, button, (ChatHudAccessor) chatHud));
    }
}
