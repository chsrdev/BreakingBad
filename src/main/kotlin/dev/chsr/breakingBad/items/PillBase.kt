package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class PillBase(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "pill_base",
    material = Material.SUGAR,
    name = "Таблеточная масса",
    color = NamedTextColor.WHITE,
    customModelData = 2502
) {
    private val recipeKey = NamespacedKey(plugin, "pill_base_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape(" S ", "MBM", " S ")
        setIngredient('S', Material.SUGAR)
        setIngredient('M', Material.MILK_BUCKET)
        setIngredient('B', Material.BONE_MEAL)
    }
}