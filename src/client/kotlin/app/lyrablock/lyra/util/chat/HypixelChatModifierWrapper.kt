package app.lyrablock.lyra.util.chat

class HypixelChatModifierWrapper(val modifier: (String) -> String) {
    companion object {
        val prefixes = arrayOf(
            "pc",
            "pchat",
            "party chat",
            "p chat",
            "gc",
            "gchat",
            "guild chat",
            "g chat",
            "ac",
            "achat"
        )
    }

    operator fun invoke(message: String): String {
        val trimmedMessage = message.trim()
        val lowerTrimmedMessage = trimmedMessage.lowercase()
        val matchingPrefix = prefixes.firstOrNull { lowerTrimmedMessage.startsWith(it.lowercase()) }

        if (matchingPrefix != null && message.length > matchingPrefix.length && message[matchingPrefix.length] == ' ') {
            val afterPrefix = trimmedMessage.substring(matchingPrefix.length).trimStart()
            return matchingPrefix + " " + modifier.invoke(afterPrefix)
        }
        return message
    }
}
