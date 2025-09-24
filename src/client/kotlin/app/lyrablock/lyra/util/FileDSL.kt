package app.lyrablock.lyra.util

import java.io.File
import java.nio.file.Path

object FileDSL {
    fun File.ensureFile() = takeUnless { exists() }?.createNewFile()
    fun File.ensurePath() = takeUnless { exists() }?.mkdirs()

    fun Path.ensurePath() = toFile().ensurePath()
}
