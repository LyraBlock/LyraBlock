package app.lyrablock.lyra.util.chat

object ChatUtils {
    val channelRegex = Regex("""(§([12b])a.+ §.> )?(?:§.\[.+?] )?(?:§7)?(.+?): (.+)""")

    fun toHypixel(original: String): HypixelMessage? {
        val match = channelRegex.find(original) ?: return null
        val channel = when(match.groupValues[2]) {
            "1" -> HypixelMessage.Channel.CHAT
            "2" -> HypixelMessage.Channel.GUILD
            "b" -> HypixelMessage.Channel.COOP
            else -> HypixelMessage.Channel.ALL
        }
        val sender = match.groupValues[3]
        val content = match.groupValues[4]
        return HypixelMessage(channel, sender, content)
    }
}
