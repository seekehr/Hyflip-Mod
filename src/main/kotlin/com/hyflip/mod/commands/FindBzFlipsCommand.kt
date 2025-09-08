package com.hyflip.mod.commands

import com.hyflip.mod.server.ServerCommunicator
import com.hyflip.mod.server.ServerCommunicator.FoundFlip
import com.hyflip.mod.utils.BazaarFlipsPaginator
import com.hyflip.mod.utils.ChatUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.event.ClickEvent

class FindBzFlipsCommand {
    companion object {
        fun execute(args: Array<String>) {
            if (args.isEmpty()) {
                ChatUtils.sendMessage(EnumChatFormatting.RED.toString() + "Args are empty??", true)
                return
            }

            if (args.size > 2) {
                if (args[1].equals("page", ignoreCase = true)) {
                    val page = args[2].toIntOrNull()
                    if (page != null) {
                        BazaarFlipsPaginator.sendPageInChat(page)
                    } else {
                        ChatUtils.sendMessage(EnumChatFormatting.RED.toString() + "Invalid page number.", true)
                    }
                    return
                }
            }

            val token = "780c1b25-d94e-48fb-8220-e436df1fd17e"

            try {
                var firstFlipPageShown = false

                BazaarFlipsPaginator.clear()
                ServerCommunicator.getBazaarFlips(
                    token,
                    Minecraft.getMinecraft().thePlayer.name,
                    onFoundFlip = { flip ->
                        println("hello")
                        BazaarFlipsPaginator.addFlip(flip)
                        if (BazaarFlipsPaginator.size() == 20 && !firstFlipPageShown) {
                            BazaarFlipsPaginator.sendFirstPageInChat()
                            firstFlipPageShown = true
                        }
                    },
                    onComplete = {
                        println("=======EQWE0IQWIEQW0IEQW==========FINISHED======")
                        // if our total flips are <20
                        if (!firstFlipPageShown) {
                            BazaarFlipsPaginator.sendFirstPageInChat()
                        }
                    },
                    onFailure = { t ->
                        ChatUtils.sendMessage("Bazaar flip request failed. Reason: ${t.toString()}", true)
                    }
                )
            } catch (e: ServerCommunicator.FlipInProgressError) {
                BazaarFlipsPaginator.sendFirstPageInChat()
            } catch (e: Exception) {
                ChatUtils.sendMessage(EnumChatFormatting.RED.toString() + "Exception: $e", true)
                println("Hyflip Connection exception: $e")
            }
        }
    }
}
