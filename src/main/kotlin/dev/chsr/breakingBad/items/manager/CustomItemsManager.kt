package dev.chsr.breakingBad.items.manager

import dev.chsr.breakingBad.items.AluminumPlate
import dev.chsr.breakingBad.items.Cannabis
import dev.chsr.breakingBad.items.CannabisSeed
import dev.chsr.breakingBad.items.ChocolateBar
import dev.chsr.breakingBad.items.ChocolateBarWithBerries
import dev.chsr.breakingBad.items.ChocolateBarWithHoney
import dev.chsr.breakingBad.items.DriedCannabis
import dev.chsr.breakingBad.items.DrugDealerBook
import dev.chsr.breakingBad.items.ElectronicBoard
import dev.chsr.breakingBad.items.EnergyDrink
import dev.chsr.breakingBad.items.EnergyMix
import dev.chsr.breakingBad.items.FarmerRecipeBook
import dev.chsr.breakingBad.items.GrowthLamp
import dev.chsr.breakingBad.items.Joint
import dev.chsr.breakingBad.items.LedMatrix
import dev.chsr.breakingBad.items.MedicalCapsule
import dev.chsr.breakingBad.items.PillBase
import dev.chsr.breakingBad.items.Reflector
import org.bukkit.plugin.java.JavaPlugin

class CustomItemsManager(private val plugin: JavaPlugin) {
    val aluminumPlate = AluminumPlate(plugin)
    val electronicBoard = ElectronicBoard(plugin)
    val ledMatrix = LedMatrix(plugin)
    val reflector = Reflector(plugin)
    val cannabisSeed = CannabisSeed(plugin)
    val cannabis = Cannabis(plugin)
    val driedCannabis = DriedCannabis(plugin)
    val joint = Joint(plugin)
    val growthLamp = GrowthLamp(plugin)
    val chocolateBar = ChocolateBar(plugin)
    val chocolateBarWithBerries = ChocolateBarWithBerries(plugin)
    val chocolateBarWithHoney = ChocolateBarWithHoney(plugin)
    val drugDealerBook = DrugDealerBook(plugin)
    val energyDrink = EnergyDrink(plugin)
    val energyMix = EnergyMix(plugin)
    val medicalCapsule = MedicalCapsule(plugin)
    val pillBase = PillBase(plugin)
    val farmerRecipeBook = FarmerRecipeBook(plugin)

    fun registerAllRecipes() {
        plugin.server.addRecipe(aluminumPlate.getRecipe())
        plugin.server.addRecipe(electronicBoard.getRecipe())
        plugin.server.addRecipe(ledMatrix.getRecipe())
        plugin.server.addRecipe(reflector.getRecipe())
        plugin.server.addRecipe(driedCannabis.getFurnaceRecipe(cannabis))
        plugin.server.addRecipe(joint.getRecipe(driedCannabis))
        plugin.server.addRecipe(growthLamp.getRecipe(aluminumPlate, reflector, electronicBoard, ledMatrix))
        plugin.server.addRecipe(chocolateBar.getRecipe())
        plugin.server.addRecipe(chocolateBarWithBerries.getRecipe())
        plugin.server.addRecipe(chocolateBarWithHoney.getRecipe())
        plugin.server.addRecipe(energyMix.getRecipe())
        plugin.server.addRecipe(energyDrink.getRecipe(energyMix))
        plugin.server.addRecipe(pillBase.getRecipe())
        plugin.server.addRecipe(medicalCapsule.getRecipe(pillBase))
    }
}