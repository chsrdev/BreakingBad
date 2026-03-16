package dev.chsr.breakingBad.helper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.components.CustomModelDataComponent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class CustomItems(plugin: JavaPlugin) {

    private val itemKey = NamespacedKey(plugin, "custom_item_id")
    private val recipeCannabisDry = NamespacedKey(plugin, "dried_cannabis_recipe")
    private val recipeJoint = NamespacedKey(plugin, "joint_recipe")

    fun createCannabisSeed(): ItemStack {
        return ItemStack(Material.BEETROOT_SEEDS).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Семена каннабиса", NamedTextColor.DARK_GREEN))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "cannabis_seed"
                )
                setCustomModelData(2001)
            }
        }
    }

    fun isCannabisSeed(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "cannabis_seed"
    }

    fun createCannabis(): ItemStack {
        return ItemStack(Material.SUGAR_CANE).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Каннабис", NamedTextColor.GREEN))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "cannabis"
                )
                setCustomModelData(2002)
            }
        }
    }

    fun isCannabis(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "cannabis"
    }

    fun createDriedCannabis(): ItemStack {
        return ItemStack(Material.DRIED_KELP).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Сушёный каннабис", NamedTextColor.GOLD))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "dried_cannabis"
                )
                setCustomModelData(2003)
            }
        }
    }

    fun isDriedCannabis(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "dried_cannabis"
    }

    fun createJoint(): ItemStack {
        return ItemStack(Material.CARROT_ON_A_STICK).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Косяк", NamedTextColor.LIGHT_PURPLE))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "joint"
                )
                setCustomModelData(2100)
            }
        }
    }

    fun isJoint(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "joint"
    }

    fun recipeCannabisDry(): FurnaceRecipe {
        val input = createCannabis()
        val result = createDriedCannabis()
        return FurnaceRecipe(
            recipeCannabisDry,
            result,
            RecipeChoice.ExactChoice(input),
            5f,
            2000
        )
    }

    fun recipeJoint(): ShapedRecipe {
        val recipe = ShapedRecipe(recipeJoint, createJoint())
        recipe.shape(
            "PPP",
            "PCP",
            "PPP"
        )

        recipe.setIngredient('C', RecipeChoice.ExactChoice(createDriedCannabis()))
        recipe.setIngredient('P', Material.PAPER)

        return recipe
    }
}