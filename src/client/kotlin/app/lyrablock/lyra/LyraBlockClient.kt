package app.lyrablock.lyra

import app.lyrablock.lyra.util.FileDSL.ensurePath
import io.ktor.client.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

object LyraBlockClient : ClientModInitializer {
    val configPath: Path = FabricLoader.getInstance().configDir.resolve("lyrablock")
    val LOGGER: Logger = LoggerFactory.getLogger("LyraBlock")
    val httpClient = HttpClient()

	override fun onInitializeClient() {
        configPath.ensurePath()
        LyraModule.load("app.lyrablock")
	}
}
