package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class LedMatrix(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "led_matrix",
    material = Material.SEA_LANTERN,
    name = "LED-матрица",
    color = NamedTextColor.AQUA,
    customModelData = 2303
) {
    private val recipeKey = NamespacedKey(plugin, "led_matrix_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("GLG", "LCL", "GLG")
        setIngredient('G', Material.GLASS)
        setIngredient('L', Material.GLOWSTONE_DUST)
        setIngredient('C', Material.COPPER_INGOT)
    }
}