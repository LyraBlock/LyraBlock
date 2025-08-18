package name.lyrablock

import name.lyrablock.feature.chat.ChatCopyHandler
import name.lyrablock.feature.mining.PickaxeAbilityCooldownTracker
import name.lyrablock.feature.misc.AotvHelper
import name.lyrablock.feature.misc.ForceSprint
import name.lyrablock.feature.misc.TpsTracker
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory

object LyraBlockClient : ClientModInitializer {
    val configPath = FabricLoader.getInstance().configDir.resolve("lyrablock")
    val LOGGER = LoggerFactory.getLogger("lyrablock")

	override fun onInitializeClient() {
		ChatCopyHandler
        TpsTracker
        AotvHelper
        ForceSprint
        PickaxeAbilityCooldownTracker
	}
}
