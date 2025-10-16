package app.lyrablock.lyra.feature.dungeon.map.room

data class SecretStatus(val current: Int, val total: Int) {
    companion object {
        val WITHOUT = SecretStatus(-1, -1)
    }
}
