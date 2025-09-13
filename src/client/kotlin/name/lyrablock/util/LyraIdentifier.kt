package name.lyrablock.util

import net.minecraft.util.Identifier

object LyraIdentifier {
    fun of(path: String) = Identifier.of("lyra", path)
}
