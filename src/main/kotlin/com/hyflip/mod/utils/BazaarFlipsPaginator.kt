package com.hyflip.mod.utils

import com.hyflip.mod.server.ServerCommunicator.FoundFlip
import com.hyflip.mod.utils.ChatUtils
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting

object BazaarFlipsPaginator {
    private val flips = mutableListOf<FoundFlip>()
    private var currentPage = 1
    private const val pageSize = 10

    fun clear() {
        flips.clear()
        currentPage = 1
    }

    fun addFlip(flip: FoundFlip) {
        flips.add(flip)
    }

    fun size(): Int = flips.size

    fun sendPageInChat(page: Int) {
        if (flips.isEmpty()) {
            ChatUtils.sendMessage(EnumChatFormatting.RED.toString() + "No flips loaded yet.", true)
            return
        }

        val maxPage = (flips.size + pageSize - 1) / pageSize
        println("total flips: ${size()} ; page: $page, maxPages: $maxPage")

        if (page !in 1..maxPage) {
            ChatUtils.sendMessage(EnumChatFormatting.RED.toString() + "Invalid page. Max page: $maxPage", true)
            return
        }

        currentPage = page
        val start = (page - 1) * pageSize
        val end = minOf(start + pageSize, flips.size)

        for (i in start until end) {
            val flip = flips[i]
            val msg = ChatComponentText(
                "${EnumChatFormatting.YELLOW}[${i + 1}] " +
                        "${EnumChatFormatting.GREEN}${flip.productId} " +
                        "${EnumChatFormatting.GRAY}Buy: ${flip.buyPrice} → Sell: ${flip.sellPrice}"
            )
            Minecraft.getMinecraft().thePlayer.addChatMessage(msg)
        }

        val buttonLine = ChatComponentText("")
        if (page > 1) {
            val prev = ChatComponentText("${EnumChatFormatting.RED}<<< ")
            prev.chatStyle = ChatStyle().setChatClickEvent(
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyf bzf page ${page - 1}")
            )
            buttonLine.appendSibling(prev)
        }

        val pageText = ChatComponentText("${EnumChatFormatting.AQUA}Page $page/$maxPage")
        buttonLine.appendSibling(pageText)

        if (page < maxPage) {
            val next = ChatComponentText("${EnumChatFormatting.GREEN} >>>")
            next.chatStyle = ChatStyle().setChatClickEvent(
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyf bzf page ${page + 1}")
            )
            buttonLine.appendSibling(next)
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(buttonLine)

    }


    fun sendFirstPageInChat() {
        sendPageInChat(1)
    }
}
