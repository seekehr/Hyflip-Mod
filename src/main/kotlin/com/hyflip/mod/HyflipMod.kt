package com.hyflip.mod

import com.hyflip.mod.commands.CommandManager
import com.hyflip.mod.config.ConfigManager
import com.hyflip.mod.config.HyflipConfig
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = HyflipMod.MOD_ID, useMetadata = true)
class HyflipMod {

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        configManager = ConfigManager()
        MinecraftForge.EVENT_BUS.register(configManager)
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        CommandManager()
    }

    // allows for static access in/of a class
    companion object {
        // lateinit is basically used to tell the compiler: "hey, this variable will defo be assigned a value later (forge Init() in our case)
        // so don't whine about it. however, it'll still throw an exception if we use configManager without init'ing it with a value first
        lateinit var configManager: ConfigManager
        const val MOD_ID = "hyflip"

        @JvmStatic // for java interoperability
        val version: String
            get() = Loader.instance().indexedModList[MOD_ID]!!.version

        val config: HyflipConfig
            get() = configManager.config ?: error("config is null")
    }
}
