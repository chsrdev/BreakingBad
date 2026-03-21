package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class ChocolateBarWithBerries(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "chocolate_bar_with_berries",
    material = Material.COOKIE,
    name = "Шоколадный батончик с ягодами",
    color = NamedTextColor.RED,
    customModelData = 2402
) {
    private val recipeKey = NamespacedKey(plugin, "chocolate_bar_with_berries_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("CBC", "MSW", "CBC")
        setIngredient('M', Material.MILK_BUCKET)
        setIngredient('S', Material.SUGAR)
        setIngredient('C', Material.COCOA_BEANS)
        setIngredient('W', Material.WHEAT)
        setIngredient('B', Material.SWEET_BERRIES)
    }
}