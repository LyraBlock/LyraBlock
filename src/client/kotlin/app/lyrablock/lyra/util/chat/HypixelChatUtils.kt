package app.lyrablock.lyra.util.chat

// TODO: Refactor this code design to improve clarity and maintainability.
object HypixelChatUtils {
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

    fun getModifierForCommands(modifier: (String) -> String): (String) -> String = { message ->
        val trimmedMessage = message.trim()
        val lowerTrimmedMessage = trimmedMessage.lowercase()
        val matchingPrefix = prefixes.firstOrNull { lowerTrimmedMessage.startsWith(it.lowercase()) }

        if (matchingPrefix != null
            && message.length > matchingPrefix.length && message[matchingPrefix.length] == ' ') {
            val afterPrefix = trimmedMessage.substring(matchingPrefix.length).trimStart()
            matchingPrefix + " " + modifier.invoke(afterPrefix)
        } else {
            message
        }
    }
}
