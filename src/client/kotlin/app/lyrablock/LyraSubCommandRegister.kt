package app.lyrablock

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object LyraSubCommandRegister {
    /**
     * Register a command like `/lyra <name>`, **without** arguments.
     */
    fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        name: String,
        executes: Command<FabricClientCommandSource>
    ) {
        // See https://docs.fabricmc.net/develop/commands/basics#client-commands
        dispatcher.register(
            literal("lyra:$name").executes(executes)
        )
        dispatcher.register(literal("ly:$name").executes(executes))
    }
}
