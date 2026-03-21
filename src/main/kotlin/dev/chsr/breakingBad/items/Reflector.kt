package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class Reflector(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "reflector",
    material = Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
    name = "Отражатель",
    color = NamedTextColor.GOLD,
    customModelData = 2304
) {
    private val recipeKey = NamespacedKey(plugin, "reflector_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("III", "G G", "III")
        setIngredient('I', Material.IRON_INGOT)
        setIngredient('G', Material.GLASS_PANE)
    }
}