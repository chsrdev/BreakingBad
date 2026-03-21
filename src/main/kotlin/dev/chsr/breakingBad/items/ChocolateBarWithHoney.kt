package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class ChocolateBarWithHoney(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "chocolate_bar_with_honey",
    material = Material.COOKIE,
    name = "Шоколадный батончик с мёдом",
    color = NamedTextColor.GOLD,
    customModelData = 2403
) {
    private val recipeKey = NamespacedKey(plugin, "chocolate_bar_with_honey_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("CHC", "MSW", "CHC")
        setIngredient('M', Material.MILK_BUCKET)
        setIngredient('S', Material.SUGAR)
        setIngredient('C', Material.COCOA_BEANS)
        setIngredient('W', Material.WHEAT)
        setIngredient('H', Material.HONEY_BOTTLE)
    }
}