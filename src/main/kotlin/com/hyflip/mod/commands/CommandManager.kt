package com.hyflip.mod.commands

import com.hyflip.mod.HyflipMod
import com.hyflip.mod.commands.HyflipCommand.ProcessCommandRunnable
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler

class CommandManager {

    init {
        registerCommandWithTabCompletion("hyflip", HyflipCommand::execute) {
            args -> listOf("help", "config", "ping")
        }
        registerCommandWithTabCompletion("hyf", HyflipCommand::execute) {
                args -> listOf("help", "config", "ping")
        }
    }


    private fun registerCommandWithTabCompletion(
        name: String,
        function: (Array<String>) -> Unit,
        autoComplete: ((Array<String>) -> List<String>) = { listOf() }
    ) {
        val command = HyflipCommand(
            name,
            createCommand(function),
            object : HyflipCommand.TabCompleteRunnable {
                override fun tabComplete(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
                    return autoComplete(args ?: emptyArray())
                }
            }
        )
        ClientCommandHandler.instance.registerCommand(command)
    }

    private fun createCommand(function: (Array<String>) -> Unit) = object : ProcessCommandRunnable() {
        override fun processCommand(sender: ICommandSender?, args: Array<String>?) {
            if (args != null) function(args.asList().toTypedArray())
        }
    }
}