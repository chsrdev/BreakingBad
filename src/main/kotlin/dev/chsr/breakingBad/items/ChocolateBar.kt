package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class ChocolateBar(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "chocolate_bar",
    material = Material.COOKIE,
    name = "Шоколадный батончик",
    color = NamedTextColor.AQUA,
    customModelData = 2401
) {
    private val recipeKey = NamespacedKey(plugin, "chocolate_bar_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("CCC", "MSW", "CCC")
        setIngredient('M', Material.MILK_BUCKET)
        setIngredient('S', Material.SUGAR)
        setIngredient('C', Material.COCOA_BEANS)
        setIngredient('W', Material.WHEAT)
    }
}