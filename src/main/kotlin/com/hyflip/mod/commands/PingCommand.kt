package com.hyflip.mod.commands

import com.hyflip.mod.server.BACKEND_URL
import com.hyflip.mod.server.DEV_BACKEND_URL
import com.hyflip.mod.utils.ChatUtils
import net.minecraft.util.EnumChatFormatting
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.measureTimeMillis

class PingCommand {
    companion object {
        fun execute(args: Array<String>) {
            var backendUrl = BACKEND_URL
            if (args.size > 1 && args[1] == "dev") {
                backendUrl = DEV_BACKEND_URL
            }

            try {
                val url = URL(backendUrl)
                val time = measureTimeMillis {
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    connection.connect()
                    connection.disconnect()
                }

                ChatUtils.sendMessage(
                    EnumChatFormatting.GREEN.toString() +
                            "Connected! Ping: " + EnumChatFormatting.DARK_PURPLE + "${time}ms.", true
                )
            } catch (e: Exception) {
                ChatUtils.sendMessage(
                    EnumChatFormatting.RED.toString() + "Not connected to Hyflip server.", true
                )
                println("Hyflip Connection exception: $e")
            }
        }
    }
}
