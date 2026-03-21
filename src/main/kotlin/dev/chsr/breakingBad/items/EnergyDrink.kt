package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class EnergyDrink(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "energy_drink",
    material = Material.POTION,
    name = "Энергетик",
    color = NamedTextColor.AQUA,
    customModelData = 2504
) {
    private val recipeKey = NamespacedKey(plugin, "energy_drink_recipe")

    fun getRecipe(energyMix: EnergyMix): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("SMS", "RRR", "SPS")
        setIngredient('S', Material.SUGAR)
        setIngredient('P', Material.GLASS_BOTTLE)
        setIngredient('M', RecipeChoice.ExactChoice(energyMix.create()))
        setIngredient('R', Material.REDSTONE)
    }
}