package com.hyflip.mod.commands

import com.hyflip.mod.HyflipMod
import com.hyflip.mod.config.ConfigManager
import com.hyflip.mod.server.BACKEND_URL
import com.hyflip.mod.server.DEV_BACKEND_URL
import com.hyflip.mod.utils.ChatUtils
import net.minecraft.util.EnumChatFormatting
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.measureTimeMillis

class SetTokenCommand {
    companion object {
        fun execute(args: Array<String>) {
            if (args.size > 1) {
                HyflipMod.config.adminCategory.token = args[1]
                HyflipMod.config.saveNow()
                ChatUtils.sendMessage(EnumChatFormatting.GREEN.toString() + "Set token to: " + HyflipMod.config.adminCategory.token + ".", true)
            } else {
                ChatUtils.sendMessage(EnumChatFormatting.RED.toString() + " Specify your token, like /hyf set <token>", true)
            }
        }
    }
}
