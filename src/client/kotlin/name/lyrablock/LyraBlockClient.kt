package name.lyrablock

import name.lyrablock.feature.chat.ChatCopyHandler
import name.lyrablock.feature.mining.PickaxeAbilityTracker
import name.lyrablock.feature.misc.AotvHelper
import name.lyrablock.feature.misc.ForceSprint
import name.lyrablock.feature.misc.TpsTracker
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader

object LyraBlockClient : ClientModInitializer {
    val configPath = FabricLoader.getInstance().configDir.resolve("lyrablock")

	override fun onInitializeClient() {
		ChatCopyHandler
        TpsTracker
        AotvHelper
        ForceSprint
        PickaxeAbilityTracker
	}
}
