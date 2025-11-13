package app.lyrablock.lyra.mixin.client;

import app.lyrablock.lyra.event.MapEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(
        method = "handleMapItemData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;applyToMap(Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;)V",
            shift = At.Shift.AFTER
        )
    )
    void lyra$apply(ClientboundMapItemDataPacket packet, CallbackInfo ci, @Local MapItemSavedData mapState) {
        MapEvents.MAP_UPDATE_APPLIED.invoker().onMapUpdateApplied(packet, mapState);
    }
}
