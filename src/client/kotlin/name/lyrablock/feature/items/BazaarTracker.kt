package app.lyrablock.feature.items

import app.lyrablock.LyraBlockClient
import app.lyrablock.LyraModule
import app.lyrablock.util.CachedLoader
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import kotlin.io.path.div
import kotlin.time.Duration.Companion.minutes

@LyraModule
@OptIn(DelicateCoroutinesApi::class)
object BazaarTracker {
    private val bazaarLoader = CachedLoader(
        BazaarData::class,
        LyraBlockClient.configPath / "bazaar.json",
        "https://api.hypixel.net/v2/skyblock/bazaar",
        rateLimit = 5.minutes
    )
    fun isProduct(id: String) = bazaarLoader.data?.products?.containsKey(id) ?: false
    fun getProduct(id: String) = bazaarLoader.data?.products?.get(id)
    fun getStatus(id: String) = getProduct(id)?.quickStatus

    init {
        ClientTickEvents.START_CLIENT_TICK.register(::onClientTick)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onClientTick(client: MinecraftClient) {
        val lastUpdated = bazaarLoader.data?.lastUpdated?.let { Instant.fromEpochMilliseconds(it) } ?: Instant.DISTANT_PAST // if data is older than 5 minutes

        if (bazaarLoader.canFetchNow() && Clock.System.now() - lastUpdated >= bazaarLoader.rateLimit) {
            GlobalScope.async { bazaarLoader.fetchAndCache() }
        }
    }


    @OptIn(ExperimentalSerializationApi::class)
    @JsonIgnoreUnknownKeys
    @Serializable
    data class BazaarData(
        val success: Boolean,
        val lastUpdated: Long,
        val products: Map<String, BazaarProduct>
    ) {
        @Serializable
        @JsonIgnoreUnknownKeys
        data class BazaarProduct(
            @SerialName("product_id")
            val productId: String,
            @SerialName("quick_status")
            val quickStatus: QuickStatus
        ) {
            @Serializable
            @JsonIgnoreUnknownKeys
            data class QuickStatus(
                val buyPrice: Double,
                val sellPrice: Double,
                val buyVolume: Int,
                val sellVolume: Int,
                val buyMovingWeek: Double,
                val sellMovingWeek: Double,
                val buyOrders: Int,
                val sellOrders: Int,
            )
        }
    }
}
