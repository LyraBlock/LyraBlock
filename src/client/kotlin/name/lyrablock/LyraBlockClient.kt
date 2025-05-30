package name.lyrablock

import name.lyrablock.feature.chat.ChatCopyHandler
import name.lyrablock.util.waypoint.WaypointController
import net.fabricmc.api.ClientModInitializer

object LyraBlockClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ChatCopyHandler
		WaypointController
	}
}