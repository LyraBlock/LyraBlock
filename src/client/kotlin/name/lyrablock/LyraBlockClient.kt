package name.lyrablock

import name.lyrablock.feature.chat.ChatCopyHandler
import name.lyrablock.feature.misc.AotvHelper
import name.lyrablock.feature.misc.TpsTracker
import name.lyrablock.util.render.waypoint.WaypointController
import net.fabricmc.api.ClientModInitializer

object LyraBlockClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ChatCopyHandler
        TpsTracker
        AotvHelper
	}
}
