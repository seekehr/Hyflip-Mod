package com.hyflip.mod.commands

import com.hyflip.mod.HyflipMod
import com.hyflip.mod.errors.CommandError
import com.hyflip.mod.server.ServerCommunicator
import com.hyflip.mod.utils.ChatUtils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting

class HyflipCommand : CommandBase {
    private val commandName: String
    private val runnable: ProcessCommandRunnable
    private var tabRunnable: TabCompleteRunnable? = null

    constructor(commandName: String, runnable: ProcessCommandRunnable) {
        this.commandName = commandName
        this.runnable = runnable
    }

    constructor(commandName: String, runnable: ProcessCommandRunnable, tabRunnable: TabCompleteRunnable?) {
        this.commandName = commandName
        this.runnable = runnable
        this.tabRunnable = tabRunnable
    }

    // we need
    companion object {
        fun execute(args: Array<String>) : Unit {
            if (args.isEmpty()) {
                HyflipMod.configManager.openConfigGui()
                return
            }

            when (args[0]) {
                "config" -> HyflipMod.configManager.openConfigGui()
                "ping" -> PingCommand.execute(args)
                "help" -> {
                    val helpMessage = EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.BOLD.toString() + "=== Hyflip ===" +
                            "\n" + EnumChatFormatting.GREEN.toString() + "-> config - Open the settings menu." +
                            "\n" + EnumChatFormatting.GREEN.toString() + "-> ping - Ping the backend server to check connectivity/speed."
                    ChatUtils.sendMessage(helpMessage)
                }
            }
        }
    }

    abstract class ProcessCommandRunnable {
        abstract fun processCommand(sender: ICommandSender?, args: Array<String>?)
    }

    interface TabCompleteRunnable {
        fun tabComplete(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String>
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }

    override fun getCommandName(): String {
        return commandName
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/$commandName help"
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        try {
            runnable.processCommand(sender, args)
        } catch (e: Throwable) {
            throw CommandError("Error while executing command /$commandName", e)
        }
    }

    override fun addTabCompletionOptions(sender: ICommandSender, args: Array<String>, pos: BlockPos): List<String>? {
        return if (tabRunnable != null) tabRunnable!!.tabComplete(sender, args, pos) else null
    }
}