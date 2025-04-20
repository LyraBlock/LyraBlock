package name.lyrablock.event

import name.lyrablock.mixin.client.ChatHudAccessor

object ClickChatEventFactory: LyraEventFactory<ClickChatEventFactory.ClickChatEvent>() {
    data class ClickChatEvent(val mouseX: Double,
                              val mouseY: Double,
                              val button: Int,
                              val chatHudAccessor: ChatHudAccessor)
}