package name.lyrablock.event

import name.lyrablock.mixin.client.ChatHudAccessor
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory


data class MouseClickData(val mouseX: Double, val mouseY: Double, val button: Int)

fun interface ClickChatCallback {
    fun onClick(mouse: MouseClickData, chatHudAccessor: ChatHudAccessor)
}

interface ClickChatEvent {
    companion object {
        @JvmField
        val EVENT: Event<ClickChatCallback> = EventFactory.createArrayBacked<ClickChatCallback>(
            ClickChatCallback::class.java
        ) { listeners ->
            ClickChatCallback { mouse, chatHudAccessor ->
                listeners.forEach {
                    it.onClick(mouse, chatHudAccessor)
                }
            }
        }
    }
}

