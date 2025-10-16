package app.lyrablock.lyra.mixin.client;

import app.lyrablock.lyra.event.MapEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.map.MapState;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(
        method = "onMapUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/packet/s2c/play/MapUpdateS2CPacket;apply(Lnet/minecraft/item/map/MapState;)V",
            shift = At.Shift.AFTER
        )
    )
    void lyra$apply(MapUpdateS2CPacket packet, CallbackInfo ci, @Local MapState mapState) {
        MapEvents.MAP_UPDATE_APPLIED.invoker().onMapUpdateApplied(packet, mapState);
    }
}
