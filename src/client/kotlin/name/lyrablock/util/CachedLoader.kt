package name.lyrablock.util

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import name.lyrablock.LyraBlockClient
import name.lyrablock.util.FileDSL.ensureFile
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@OptIn(DelicateCoroutinesApi::class)
class CachedLoader<T : Any>(
    private val kClass: KClass<T>,
    localPath: Path,
    private val url: String,
    private val rateLimit: Duration = 5.minutes
) {
    private val local = localPath.toFile()
    private var lastFetch: TimeMarker = TimeMarker()

    var data: T? = null
        private set

    init {
        local.ensureFile()
        data = loadLocalData()
        if (data == null) GlobalScope.launch {
            data = fetchAndCache()
        }
    }

    fun canFetchNow() = lastFetch.untilNow() >= rateLimit

    @Suppress("unchecked_cast")
    fun loadLocalData(): T? = runCatching {
        val serializer = Json.serializersModule.serializer(kClass.createType()) as KSerializer<T>
        Json.decodeFromString(serializer, local.readText())
    }.getOrNull()

    @Suppress("unchecked_cast")
    suspend fun fetchAndCache(): T? = runCatching {
        if (lastFetch.untilNow() < rateLimit) return null

        val response = LyraBlockClient.httpClient.get(url)

        local.outputStream().use {
            response.bodyAsChannel().copyAndClose(it.asByteWriteChannel())
        }
        lastFetch.mark()
        return loadLocalData()

    }.getOrNull()
}
