package com.hyflip.mod.config

import com.google.gson.annotations.Expose
import com.hyflip.mod.HyflipMod
import com.hyflip.mod.config.categories.AdminCategory
import com.hyflip.mod.config.categories.BZConfig
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category

class HyflipConfig : Config() {

    override fun getTitle(): String {
        return "§eHyflip §fconfig (V${HyflipMod.version})"
    }

    override fun saveNow() {
        HyflipMod.configManager.save()
    }

    @JvmField
    @Expose // for gson to serialize
    @Category(name = "Flippers", desc = "Manage/toggle auction house and bazaar flippers.")
    var adminCategory: AdminCategory = AdminCategory()

    @JvmField
    @Expose
    @Category(name = "Bazaar Flipper", desc = "Configure the bazaar flipper.")
    var bzCategory: BZConfig = BZConfig()
}
