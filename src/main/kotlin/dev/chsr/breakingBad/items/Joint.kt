package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class Joint(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "joint",
    material = Material.CARROT_ON_A_STICK,
    name = "Косяк",
    color = NamedTextColor.LIGHT_PURPLE,
    customModelData = 2100
) {
    private val recipeKey = NamespacedKey(plugin, "joint_recipe")

    fun getRecipe(driedCannabis: DriedCannabis): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape("PPP", "PCP", "PPP")
        setIngredient('C', RecipeChoice.ExactChoice(driedCannabis.create()))
        setIngredient('P', Material.PAPER)
    }
}