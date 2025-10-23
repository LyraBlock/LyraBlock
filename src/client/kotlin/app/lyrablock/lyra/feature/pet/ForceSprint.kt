package app.lyrablock.feature.misc

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.base.HypixelInfo
import app.lyrablock.lyra.event.KeyBindingOverrideCallback

// Although we have toggle sprint in vanilla by default, force sprinting looks VERY nice!
@LyraModule
object ForceSprint {
    init {
        KeyBindingOverrideCallback.EVENT.register(::overrideSprint)
    }

    fun overrideSprint(translateKey: String): Boolean? {
        if (translateKey != "key.sprint" || !HypixelInfo.isOnSkyBlock) return null

        return true
    }
}
