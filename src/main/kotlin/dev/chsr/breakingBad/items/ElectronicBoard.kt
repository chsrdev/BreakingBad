package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class ElectronicBoard(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "electronic_board",
    material = Material.REPEATER,
    name = "Электронная плата",
    color = NamedTextColor.YELLOW,
    customModelData = 2302
) {
    private val recipeKey = NamespacedKey(plugin, "electronic_board_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("CRC", "QRI", "CRC")
        setIngredient('C', Material.COPPER_INGOT)
        setIngredient('R', Material.REDSTONE)
        setIngredient('Q', Material.QUARTZ)
        setIngredient('I', Material.IRON_INGOT)
    }
}