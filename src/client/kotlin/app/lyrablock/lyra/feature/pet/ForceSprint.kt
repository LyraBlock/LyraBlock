package app.lyrablock.feature.misc

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.event.KeyBindingOverrideCallback
import app.lyrablock.lyra.util.SkyblockUtils

// Although we have toggle sprint in vanilla by default, force sprinting looks VERY nice!
@LyraModule
object ForceSprint {
    init {
        KeyBindingOverrideCallback.EVENT.register(::overrideSprint)
    }

    fun overrideSprint(translateKey: String): Boolean? {
        if (translateKey != "key.sprint" || !SkyblockUtils.isInSkyBlock()) return null

        return true
    }
}
