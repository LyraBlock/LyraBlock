package name.lyrablock.event

import net.fabricmc.fabric.api.event.EventFactory

fun interface KeyBindingOverrideCallback {
    fun override(translateKey: String): Boolean?

    companion object {
        @JvmField
        val EVENT = EventFactory.createArrayBacked(
            KeyBindingOverrideCallback::class.java
        ) { listeners ->
            KeyBindingOverrideCallback { translateKey ->
                listeners.forEach {
                    val result = it.override(translateKey)
                    if (result != null) return@KeyBindingOverrideCallback result
                }
                null
            }
        }!!
    }
}
