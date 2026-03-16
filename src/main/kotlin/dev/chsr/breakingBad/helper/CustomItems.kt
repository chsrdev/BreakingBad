package dev.chsr.breakingBad.helper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class CustomItems(plugin: JavaPlugin) {

    private val itemKey = NamespacedKey(plugin, "custom_item_id")
    private val driedCannabisRecipeKey = NamespacedKey(plugin, "dried_cannabis_recipe")
    private val jointRecipeKey = NamespacedKey(plugin, "joint_recipe")

    private fun ItemStack.setCmd(value: Int) {
        val meta = itemMeta ?: return
        val component = meta.customModelDataComponent
        component.floats = listOf(value.toFloat())
        meta.setCustomModelDataComponent(component)
        itemMeta = meta
    }

    private fun buildItem(
        material: Material,
        name: String,
        color: NamedTextColor,
        id: String,
        cmd: Int
    ): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item

        meta.displayName(Component.text(name, color))
        meta.lore(listOf(Component.text("BreakingBad", NamedTextColor.GRAY)))
        meta.persistentDataContainer.set(itemKey, PersistentDataType.STRING, id)
        item.itemMeta = meta

        item.setCmd(cmd)
        return item
    }

    fun createCannabisSeed(): ItemStack =
        buildItem(
            material = Material.BEETROOT_SEEDS,
            name = "Семена каннабиса",
            color = NamedTextColor.DARK_GREEN,
            id = "cannabis_seed",
            cmd = 2001
        )

    fun isCannabisSeed(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "cannabis_seed"
    }

    fun createCannabis(): ItemStack =
        buildItem(
            material = Material.SUGAR_CANE,
            name = "Каннабис",
            color = NamedTextColor.GREEN,
            id = "cannabis",
            cmd = 2002
        )

    fun isCannabis(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "cannabis"
    }

    fun createDriedCannabis(): ItemStack =
        buildItem(
            material = Material.DRIED_KELP,
            name = "Сушёный каннабис",
            color = NamedTextColor.GOLD,
            id = "dried_cannabis",
            cmd = 2003
        )

    fun isDriedCannabis(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "dried_cannabis"
    }

    fun createJoint(): ItemStack =
        buildItem(
            material = Material.CARROT_ON_A_STICK,
            name = "Косяк",
            color = NamedTextColor.LIGHT_PURPLE,
            id = "joint",
            cmd = 2100
        )

    fun isJoint(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "joint"
    }

    fun recipeCannabisDry(): FurnaceRecipe {
        return FurnaceRecipe(
            driedCannabisRecipeKey,
            createDriedCannabis(),
            RecipeChoice.ExactChoice(createCannabis()),
            5f,
            2000
        )
    }

    fun recipeJoint(): ShapedRecipe {
        return ShapedRecipe(jointRecipeKey, createJoint()).apply {
            shape(
                "PPP",
                "PCP",
                "PPP"
            )
            setIngredient('C', RecipeChoice.ExactChoice(createDriedCannabis()))
            setIngredient('P', Material.PAPER)
        }
    }
}