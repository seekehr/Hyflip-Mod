package com.hyflip.mod.config;

import com.hyflip.mod.HyflipMod;
import com.google.gson.annotations.Expose;
import com.hyflip.mod.config.categories.AdminCategory;
import com.hyflip.mod.config.categories.BZConfig;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;

public class HyflipConfig extends Config {

    @Override
    public String getTitle() {
        return "§eHyflip §fconfig (V" + HyflipMod.getVersion() + ")";
    }

    @Override
    public void saveNow() {
        HyflipMod.configManager.save();
    }

    @Expose // for gson to serialise
    @Category(name = "Flippers", desc = "Manage/toggle auction house and bazaar flippers.")
    public AdminCategory adminCategory = new AdminCategory();

    @Expose
    @Category(name = "Bazaar Flipper", desc = "Configure the bazaar flipper.")
    public BZConfig bzCategory = new BZConfig();
}
