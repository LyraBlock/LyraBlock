package app.lyrablock.lyra.util

import net.minecraft.resources.ResourceLocation

object LyraIdentifier {
    fun of(path: String) = ResourceLocation.fromNamespaceAndPath("lyra", path)
}
