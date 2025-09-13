package name.lyrablock

import name.lyrablock.base.LyraTitleController
import name.lyrablock.feature.chat.ChatCopyHandler
import name.lyrablock.feature.display.SpeedTracker
import name.lyrablock.feature.mining.PickaxeAbilityCooldownTracker
import name.lyrablock.feature.mining.SnailHelper
import name.lyrablock.feature.misc.AotvHelper
import name.lyrablock.feature.misc.ForceSprint
import name.lyrablock.feature.misc.TpsTracker
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

object LyraBlockClient : ClientModInitializer {
    val configPath: Path = FabricLoader.getInstance().configDir.resolve("lyrablock")
    val LOGGER: Logger = LoggerFactory.getLogger("LyraBlock")

	override fun onInitializeClient() {
		ChatCopyHandler
        TpsTracker
        AotvHelper
        ForceSprint
        PickaxeAbilityCooldownTracker
        SnailHelper
        SpeedTracker
        LyraTitleController
	}
}
