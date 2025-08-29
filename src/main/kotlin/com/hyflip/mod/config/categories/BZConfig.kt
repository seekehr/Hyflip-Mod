package com.hyflip.mod.config.categories

import com.google.gson.annotations.Expose
import com.hyflip.mod.utils.ChatUtils
import io.github.notenoughupdates.moulconfig.annotations.*

class BZConfig {

    @JvmField
    @Expose
    @ConfigOption(name = "Min Profit", desc = "Minimum profit")
    @ConfigEditorText
    var minProfit: String = "0" // parse to int

    @JvmField
    @Expose
    @ConfigOption(name = "Min Profit Percentage", desc = "Minimum profit percentage")
    @ConfigEditorSlider(minValue = 5F, maxValue = 100F, minStep = 5F)
    var minProfitPercentage: Int = 5

    @JvmField
    @Expose
    @ConfigOption(name = "Include Craft Cost", desc = "Include crafting cost in calculation")
    @ConfigEditorBoolean
    var includeCraftCost: Boolean = false

    @JvmField
    @Expose
    @ConfigOption(name = "Min Volume Difference", desc = "Minimum volume difference")
    @ConfigEditorText
    var minVolumeDiff: String = "0" // parse to int

    @JvmField
    @Expose
    @ConfigOption(name = "Min Buy Volume", desc = "Minimum buy volume")
    @ConfigEditorText
    var minBuyVolume: String = "0" // parse to int

    @JvmField
    @Expose
    @ConfigOption(name = "Min Sell Moving Week", desc = "Minimum sell moving week")
    @ConfigEditorText
    var minSellMovingWeek: String = "0" // parse to int

    @JvmField
    @Expose
    @ConfigOption(name = "Min Buy Moving Week", desc = "Minimum buy moving week")
    @ConfigEditorText
    var minBuyMovingWeek: String = "0" // parse to int

    @JvmField
    @Expose
    @ConfigOption(name = "Min Insta Buys", desc = "Minimum instant buys")
    @ConfigEditorText
    var minInstaBuys: String = "0" // parse to int

    @JvmField
    @Expose
    @ConfigOption(name = "Max Insta Sells", desc = "Maximum instant sells")
    @ConfigEditorText
    var maxInstaSells: String = "0" // parse to int

    @JvmField
    @Expose
    var excludeItems: ArrayList<String> = ArrayList()
}