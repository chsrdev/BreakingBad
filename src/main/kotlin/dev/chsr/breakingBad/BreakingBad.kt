package dev.chsr.breakingBad

import dev.chsr.breakingBad.addiction.AddictionManager
import dev.chsr.breakingBad.addiction.AddictionStorage
import dev.chsr.breakingBad.command.AddictionCommand
import dev.chsr.breakingBad.command.DrugCommand
import dev.chsr.breakingBad.helper.CropStorage
import dev.chsr.breakingBad.helper.CustomItems
import dev.chsr.breakingBad.helper.GrowthLampManager
import dev.chsr.breakingBad.helper.GrowthLampTask
import dev.chsr.breakingBad.helper.LampStorage
import dev.chsr.breakingBad.listeners.CannabisListener
import dev.chsr.breakingBad.listeners.DrugDealerListener
import dev.chsr.breakingBad.listeners.GrowthLampListener
import dev.chsr.breakingBad.listeners.VillagerTradeListener
import me.example.myplugin.CannabisSeedListener
import org.bukkit.plugin.java.JavaPlugin

class BreakingBad : JavaPlugin() {
    lateinit var customItems: CustomItems
    lateinit var cropStorage: CropStorage
    lateinit var addictionManager: AddictionManager
    lateinit var addictionStorage: AddictionStorage
    lateinit var lampStorage: LampStorage
    lateinit var lampManager: GrowthLampManager
    lateinit var growthLampTask: GrowthLampTask

    override fun onEnable() {
        customItems = CustomItems(this)
        cropStorage = CropStorage(this)
        addictionStorage = AddictionStorage(this)
        addictionManager = AddictionManager(this, addictionStorage)
        lampStorage = LampStorage(this)
        lampManager = GrowthLampManager(this)
        growthLampTask = GrowthLampTask(this, lampManager, cropStorage)

        server.pluginManager.registerEvents(
            CannabisSeedListener(customItems, cropStorage),
            this
        )
        server.pluginManager.registerEvents(
            CannabisListener(this, customItems, addictionManager),
            this
        )
        server.pluginManager.registerEvents(
            DrugDealerListener(this, customItems),
            this
        )
        server.pluginManager.registerEvents(
            VillagerTradeListener(this, customItems),
            this
        )
        server.pluginManager.registerEvents(
            GrowthLampListener(customItems, lampManager, cropStorage),
            this
        )

        addictionManager.startTasks()
        growthLampTask.startTasks()

        val drugCommand = DrugCommand(customItems)
        getCommand("drug")?.setExecutor(drugCommand)
        getCommand("drug")?.tabCompleter = drugCommand

        val addictionCommand = AddictionCommand(addictionManager)
        getCommand("addiction")?.setExecutor(addictionCommand)
        getCommand("addiction")?.tabCompleter = addictionCommand

        server.addRecipe(customItems.recipeCannabisDry())
        server.addRecipe(customItems.recipeJoint())
        server.addRecipe(customItems.recipeAluminumPlate())
        server.addRecipe(customItems.recipeElectronicBoard())
        server.addRecipe(customItems.recipeLedMatrix())
        server.addRecipe(customItems.recipeReflector())
        server.addRecipe(customItems.recipeGrowthLamp())
    }

    override fun onDisable() {
        cropStorage.save()
        addictionStorage.save()
        lampStorage.save()
    }
}
