package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class MedicalCapsule(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "medical_capsule",
    material = Material.RABBIT_FOOT,
    name = "Таблетка",
    color = NamedTextColor.WHITE,
    customModelData = 2503
) {
    private val recipeKey = NamespacedKey(plugin, "medical_capsule_recipe")

    fun getRecipe(pillBase: PillBase): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape(" S ", "PMP", " S ")
        setIngredient('S', Material.SUGAR)
        setIngredient('P', RecipeChoice.ExactChoice(pillBase.create()))
        setIngredient('M', Material.GLISTERING_MELON_SLICE)
    }
}