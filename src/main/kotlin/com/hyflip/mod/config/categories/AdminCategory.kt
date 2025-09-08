package com.hyflip.mod.config.categories

import com.google.gson.annotations.Expose
import com.hyflip.mod.utils.ChatUtils
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class AdminCategory {

    @JvmField
    @Expose
    @ConfigOption(name = "Bazaar Flipper Toggle", desc = "Toggle the bazaar flipping module.")
    @ConfigEditorBoolean
    var bzToggle: Boolean = false

    @JvmField
    @Expose
    @ConfigOption(name = "AH Flipping Toggle", desc = "Toggle the auction house flipping module. (WIP)")
    @ConfigEditorBoolean
    var ahToggle: Boolean = false

    @JvmField
    @ConfigOption(name = "Sync Config", desc = "Click to pull your config from the backend IF you made changes on the website.")
    @ConfigEditorButton(buttonText = "Sync")
    var syncConfig: Runnable = Runnable {
        ChatUtils.sendMessage("Good boy", true)
    }
}