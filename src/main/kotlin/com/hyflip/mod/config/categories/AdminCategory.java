package com.hyflip.mod.config.categories;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class AdminCategory {

    @Expose
    @ConfigOption(name = "Bazaar Flipper Toggle", desc = "Toggle the bazaar flipping module.")
    @ConfigEditorBoolean
    public boolean bzToggle = false;

    @Expose
    @ConfigOption(name = "AH Flipping Toggle", desc = "Toggle the auction house flipping module. (WIP)")
    @ConfigEditorBoolean
    public boolean ahToggle = false;
}
