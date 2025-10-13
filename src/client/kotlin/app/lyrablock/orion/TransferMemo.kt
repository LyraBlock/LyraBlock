package app.lyrablock.orion

class TransferMemo() {

    private val cache = mutableMapOf<String, Any?>()

    // delegate provider
    operator fun <T> invoke(key: String? = null, initializer: () -> T): Lazy<T> {
        val actualKey = key ?: initializer.hashCode().toString()

        return lazy {
            @Suppress("UNCHECKED_CAST")
            cache.getOrPut(actualKey) { initializer() } as T
        }
    }

    companion object {
        private val instance = TransferMemo()
        fun create(): TransferMemo = instance
    }
}
