package app.lyrablock.lyra.event

import net.fabricmc.fabric.api.event.EventFactory

object FovEvents {
    fun interface MultiplyFov {
        fun multiply(tickDelta: Float): Float
    }

    @JvmField
    val MULTIPLY = EventFactory.createArrayBacked(MultiplyFov::class.java) { listeners ->
        MultiplyFov { tickDelta ->
            listeners.fold(1f) { fov, listener ->
                fov * listener.multiply(tickDelta)
            }
        }
    }!!
}
