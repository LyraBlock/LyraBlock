package name.lyrablock

import name.lyrablock.chat.ChatCopyHandler
import net.fabricmc.api.ClientModInitializer

object LyraBlockClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ChatCopyHandler
	}
}