package com.hyflip.mod.utils

import com.hyflip.mod.server.ServerCommunicator.FoundFlip
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import kotlin.math.floor

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

    fun sendPageInChat(page: Int, firstPage: Boolean = false) {
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
                        "${EnumChatFormatting.LIGHT_PURPLE}${EnumChatFormatting.BOLD}${flip.productId} " +
                        "${EnumChatFormatting.DARK_AQUA}Buy: ${EnumChatFormatting.WHITE}${EnumChatFormatting.BOLD}${ItemUtils.formatPrice(flip.buyPrice)}" +
                        " ${EnumChatFormatting.RESET}→ " +
                        "${EnumChatFormatting.DARK_AQUA}Sell: ${EnumChatFormatting.WHITE}${EnumChatFormatting.BOLD}${ItemUtils.formatPrice(flip.sellPrice)}${EnumChatFormatting.RESET}"
            )

            println(msg)

            val searchableItemName = ItemUtils.convertItemIdToSearchableName(flip.productId)
            msg.chatStyle = ChatStyle()
                .setChatHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(
                    EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "SellVol" +  " » " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + flip.sellVolume + "\n" +
                            EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "BuyVol" +  " » "  + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + flip.buyVolume + "\n" +
                            EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "SellMovWeek" +  " » " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + flip.sellMovingWeek + "\n" +
                            EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "BuyMovWeek" +  " » " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + flip.buyMovingWeek + "\n" +
                            EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "RecommendedVolFlip" +  " » " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + flip.recommendedFlipVolume + "\n" +
                            EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "RecommendedVolFlipProfit" +  " » " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + flip.profitFromRecommendedFlipVolume
                )))
                .setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bzs $searchableItemName"))

            Minecraft.getMinecraft().thePlayer.addChatMessage(msg)
        }

        val buttonLine = ChatComponentText("")
        if (page > 1) {
            val prev = ChatComponentText("${EnumChatFormatting.RED}«««")
            prev.chatStyle = ChatStyle().setChatClickEvent(
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyf bzf page ${page - 1}")
            )
            buttonLine.appendSibling(prev)
        }

        val pageText: ChatComponentText = if (firstPage) {
            ChatComponentText("${EnumChatFormatting.AQUA}Page $page/??")
        } else {
            ChatComponentText("${EnumChatFormatting.AQUA}Page $page/$maxPage")
        }

        buttonLine.appendSibling(pageText)

        if (page < maxPage) {
            val next = ChatComponentText("${EnumChatFormatting.GREEN} »»»")
            next.chatStyle = ChatStyle().setChatClickEvent(
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyf bzf page ${page + 1}")
            )
            buttonLine.appendSibling(next)
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(buttonLine)

    }


    fun sendFirstPageInChat() {
        sendPageInChat(1, true)
    }
}
