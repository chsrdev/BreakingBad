package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class GrowthLamp(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "growth_lamp",
    material = Material.SHROOMLIGHT,
    name = "Фитолампа",
    color = NamedTextColor.AQUA
) {
    private val recipeKey = NamespacedKey(plugin, "growth_lamp_recipe")

    fun getRecipe(
        aluminumPlate: AluminumPlate,
        reflector: Reflector,
        electronicBoard: ElectronicBoard,
        ledMatrix: LedMatrix
    ): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("ARA", "BLB", "ACA")
        setIngredient('A', RecipeChoice.ExactChoice(aluminumPlate.create()))
        setIngredient('R', RecipeChoice.ExactChoice(reflector.create()))
        setIngredient('B', RecipeChoice.ExactChoice(electronicBoard.create()))
        setIngredient('L', RecipeChoice.ExactChoice(ledMatrix.create()))
        setIngredient('C', Material.REDSTONE_LAMP)
    }
}