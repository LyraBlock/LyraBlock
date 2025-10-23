package app.lyrablock.lyra.base

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.util.MCUtils
import net.hypixel.data.region.Environment
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket
import kotlin.jvm.optionals.getOrNull

@LyraModule
object HypixelInfo {
    val hypixel = HypixelModAPI.getInstance()!!

    init {
        // If Hypixel does not send sound data, fuck them.
        hypixel.createHandler(ClientboundLocationPacket::class.java, ::onLocationPacket)
        hypixel.createHandler(ClientboundPartyInfoPacket::class.java, ::onPartyInfoPacket)
        hypixel.createHandler(ClientboundHelloPacket::class.java, ::onHello)
    }

    var serverType: String? = null
        private set

    // Raw location (in string)
    var rawLocation: String? = null
        private set
    // Skyblock location
    var location: SkyblockLocation? = null
        private set
    var partySize: Int = 0
        private set
    var isInParty: Boolean = false
        private set
    var isPartyLeader: Boolean = false
        private set
    var environment: Environment? = null
        private set

    val isOnSkyBlock get() = serverType == "SKYBLOCK"
    val isInDungeons get() = location == SkyblockLocation.DUNGEON

    private fun onLocationPacket(packet: ClientboundLocationPacket) {
        serverType = packet.serverType.getOrNull()?.name
        rawLocation = packet.mode.getOrNull()
        location = SkyblockLocation.fromId(rawLocation)
    }

    private fun onPartyInfoPacket(packet: ClientboundPartyInfoPacket) {
        isInParty = packet.isInParty
        partySize = packet.members.size
        isPartyLeader = packet.leader.isPresent && packet.leader.get() == MCUtils.thePlayer?.uuid
    }

    private fun onHello(packet: ClientboundHelloPacket) {
        environment = packet.environment
        // Every time the player rejoin the server we should invalidate the party info.
        hypixel.sendPacket(ServerboundPartyInfoPacket())
    }
}
