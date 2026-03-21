package dev.chsr.breakingBad.items;

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin;

class EnergyMix(plugin:JavaPlugin) : CustomItem(
        plugin = plugin,
        id = "energy_mix",
        material = Material.SUGAR,
        name = "Энергетическая смесь",
        color = NamedTextColor.GOLD,
        customModelData = 2501
) {
    private val recipeKey = NamespacedKey(plugin, "energy_mix_recipe")

    fun getRecipe(): ShapedRecipe = ShapedRecipe(recipeKey, create()).apply {
        shape(" S ", "RGR", " S ")
        setIngredient('S', Material.SUGAR)
        setIngredient('R', Material.REDSTONE)
        setIngredient('G', Material.GLOWSTONE_DUST)
    }
}