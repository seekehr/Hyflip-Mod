package com.hyflip.mod.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting

// object keyword means a single instance of the class is created by the compiler and shared globally. it's a much better
// alternative to static classes. so a singleton basically... neat
object ChatUtils {
    val Prefix = EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.BOLD + "Hyflip> " + EnumChatFormatting.RESET

    fun sendMessage(message: String, keepPrefix: Boolean) {
        if (keepPrefix) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(Prefix + message))
            return
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(message))
    }
}