package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.plugin.java.JavaPlugin

class DriedCannabis(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "dried_cannabis",
    material = Material.DRIED_KELP,
    name = "Сушёный каннабис",
    color = NamedTextColor.GOLD,
    customModelData = 2003
) {
    private val recipeKey = NamespacedKey(plugin, "dried_cannabis_recipe")

    fun getFurnaceRecipe(cannabis: Cannabis): FurnaceRecipe = FurnaceRecipe(
        recipeKey,
        create(),
        RecipeChoice.ExactChoice(cannabis.create()),
        5f,
        20 * 180
    )
}