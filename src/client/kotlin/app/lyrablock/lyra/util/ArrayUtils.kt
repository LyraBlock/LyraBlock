package app.lyrablock.lyra.util

object ArrayUtils {
    fun ByteArray.chunked(chunkSize: Int): Array<ByteArray> {
        if (chunkSize <= 0) throw IllegalArgumentException("Chunk size must be positive")

        val result = mutableListOf<ByteArray>()
        var offset = 0
        val size = this.size

        while (offset < size) {
            val end = minOf(offset + chunkSize, size)

            val chunk = this.copyOfRange(offset, end)

            result.add(chunk)
            offset += chunkSize
        }

        return result.toTypedArray()
    }
}
