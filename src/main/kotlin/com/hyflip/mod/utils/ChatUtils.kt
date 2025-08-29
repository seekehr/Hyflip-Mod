package com.hyflip.mod.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

// object keyword means a single instance of the class is created by the compiler and shared globally. it's a much better
// alternative to static classes. so a singleton basically... neat
object ChatUtils {
    fun sendMessage(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(message))
    }
}