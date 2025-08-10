package name.lyrablock.feature.misc

import name.lyrablock.LyraModule
import name.lyrablock.event.KeyBindingOverrideCallback
import name.lyrablock.util.SkyBlockUtils

// Although we have toggle sprint in vanilla by default, force sprinting looks VERY nice!
@LyraModule
object ForceSprint {
    init {
        KeyBindingOverrideCallback.EVENT.register(::overrideSprint)
    }

    fun overrideSprint(translateKey: String): Boolean? {
        if (translateKey != "key.sprint") return null
        if (!SkyBlockUtils.isInSkyBlock()) return null

        return true
    }
}
