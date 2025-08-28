package com.hyflip.mod.config.categories;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

import java.util.ArrayList;
import java.util.List;

public class BZConfig {

    @Expose
    @ConfigOption(name = "Min Profit", desc = "Minimum profit")
    @ConfigEditorText
    public String minProfit = "0"; // parse to String

    @Expose
    @ConfigOption(name = "Min Profit Percentage", desc = "Minimum profit percentage")
    @ConfigEditorSlider(minValue = 5, maxValue = 100, minStep = 5)
    public Integer minProfitPercentage = 5;

    /*@Expose
    @ConfigOption(name = "Exclude Items", desc = "Items to exclude")
    @Config
    public List<String> excludeItems = new ArrayList<>();*/

    @Expose
    @ConfigOption(name = "Include Craft Cost", desc = "Include crafting cost in calculation")
    @ConfigEditorBoolean
    public boolean includeCraftCost = false;

    @Expose
    @ConfigOption(name = "Min Volume Difference", desc = "Minimum volume difference")
    @ConfigEditorText
    public String minVolumeDiff = "0"; // parse to String

    @Expose
    @ConfigOption(name = "Min Buy Volume", desc = "Minimum buy volume")
    @ConfigEditorText
    public String minBuyVolume = "0"; // parse to String

    @Expose
    @ConfigOption(name = "Min Sell Moving Week", desc = "Minimum sell moving week")
    @ConfigEditorText
    public String minSellMovingWeek = "0"; // parse to String

    @Expose
    @ConfigOption(name = "Min Buy Moving Week", desc = "Minimum buy moving week")
    @ConfigEditorText
    public String minBuyMovingWeek = "0"; // parse to String

    @Expose
    @ConfigOption(name = "Min Insta Buys", desc = "Minimum instant buys")
    @ConfigEditorText
    public String minInstaBuys = "0"; // parse to String

    @Expose
    @ConfigOption(name = "Max Insta Sells", desc = "Maximum instant sells")
    @ConfigEditorText
    public String maxInstaSells = "0"; // parse to String
}
