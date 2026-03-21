package dev.chsr.breakingBad

import dev.chsr.breakingBad.addiction.AddictionManager
import dev.chsr.breakingBad.addiction.AddictionStorage
import dev.chsr.breakingBad.command.AddictionCommand
import dev.chsr.breakingBad.command.DrugCommand
import dev.chsr.breakingBad.helper.*
import dev.chsr.breakingBad.items.manager.CustomItemsManager
import dev.chsr.breakingBad.listeners.CannabisListener
import dev.chsr.breakingBad.listeners.ChocolateBarListener
import dev.chsr.breakingBad.listeners.DrugDealerListener
import dev.chsr.breakingBad.listeners.GrowthLampListener
import dev.chsr.breakingBad.listeners.VillagerTradeListener
import me.example.myplugin.CannabisSeedListener
import org.bukkit.plugin.java.JavaPlugin

class BreakingBad : JavaPlugin() {
    lateinit var customItemsManager: CustomItemsManager
    lateinit var cropStorage: CropStorage
    lateinit var addictionManager: AddictionManager
    lateinit var addictionStorage: AddictionStorage
    lateinit var lampStorage: LampStorage
    lateinit var lampManager: GrowthLampManager
    lateinit var growthLampTask: GrowthLampTask

    override fun onEnable() {
        customItemsManager = CustomItemsManager(this)
        cropStorage = CropStorage(this)
        addictionStorage = AddictionStorage(this)
        addictionManager = AddictionManager(this, addictionStorage)
        lampStorage = LampStorage(this)
        lampManager = GrowthLampManager(this)
        growthLampTask = GrowthLampTask(this, lampManager, cropStorage)

        registerEvents()

        addictionManager.startTasks()
        growthLampTask.startTasks()

        val drugCommand = DrugCommand(customItemsManager)
        getCommand("drug")?.setExecutor(drugCommand)
        getCommand("drug")?.tabCompleter = drugCommand

        val addictionCommand = AddictionCommand(addictionManager)
        getCommand("addiction")?.setExecutor(addictionCommand)
        getCommand("addiction")?.tabCompleter = addictionCommand

        customItemsManager.registerAllRecipes()
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(
            CannabisSeedListener(customItemsManager, cropStorage),
            this
        )
        server.pluginManager.registerEvents(
            CannabisListener(this, customItemsManager, addictionManager),
            this
        )
        server.pluginManager.registerEvents(
            DrugDealerListener(this, customItemsManager),
            this
        )
        server.pluginManager.registerEvents(
            VillagerTradeListener(this, customItemsManager),
            this
        )
        server.pluginManager.registerEvents(
            GrowthLampListener(customItemsManager, lampManager, cropStorage),
            this
        )
        server.pluginManager.registerEvents(
            ChocolateBarListener(this, customItemsManager),
            this
        )
    }

    override fun onDisable() {
        cropStorage.save()
        addictionStorage.save()
        lampStorage.save()
    }
}
