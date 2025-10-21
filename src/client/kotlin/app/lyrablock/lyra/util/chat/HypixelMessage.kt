package app.lyrablock.lyra.util.chat

class HypixelMessage(val channel: Channel, val sender: String, val content: String) {
    enum class Channel {
        ALL,
        CHAT,
        GUILD,
        COOP;
    }
}
