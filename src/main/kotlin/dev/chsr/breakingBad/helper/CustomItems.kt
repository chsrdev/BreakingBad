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
    private val recipeGrowthLamp = NamespacedKey(plugin, "growth_lamp_recipe")
    private val recipeAluminumPlate = NamespacedKey(plugin, "aluminum_plate_recipe")
    private val recipeElectronicBoard = NamespacedKey(plugin, "electronic_board_recipe")
    private val recipeLedMatrix = NamespacedKey(plugin, "led_matrix_recipe")
    private val recipeReflector = NamespacedKey(plugin, "reflector_recipe")

    fun createAluminumPlate(): ItemStack {
        return ItemStack(Material.IRON_NUGGET).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Алюминиевая пластина", NamedTextColor.WHITE))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "aluminum_plate"
                )
                setCustomModelData(2301)
            }
        }
    }

    fun isAluminumPlate(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "aluminum_plate"
    }

    fun recipeAluminumPlate(): ShapedRecipe {
        val recipe = ShapedRecipe(recipeAluminumPlate, createAluminumPlate())
        recipe.shape(
            "III",
            "I I",
            "III"
        )
        recipe.setIngredient('I', Material.IRON_INGOT)
        return recipe
    }

    fun createElectronicBoard(): ItemStack {
        return ItemStack(Material.REPEATER).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Электронная плата", NamedTextColor.YELLOW))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "electronic_board"
                )
                setCustomModelData(2302)
            }
        }
    }

    fun isElectronicBoard(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "electronic_board"
    }

    fun recipeElectronicBoard(): ShapedRecipe {
        val recipe = ShapedRecipe(recipeElectronicBoard, createElectronicBoard())
        recipe.shape(
            "CRC",
            "QRI",
            "CRC"
        )
        recipe.setIngredient('C', Material.COPPER_INGOT)
        recipe.setIngredient('R', Material.REDSTONE)
        recipe.setIngredient('Q', Material.QUARTZ)
        recipe.setIngredient('I', Material.IRON_INGOT)
        return recipe
    }

    fun createLedMatrix(): ItemStack {
        return ItemStack(Material.SEA_LANTERN).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("LED-матрица", NamedTextColor.AQUA))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "led_matrix"
                )
                setCustomModelData(2303)
            }
        }
    }

    fun isLedMatrix(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "led_matrix"
    }

    fun recipeLedMatrix(): ShapedRecipe {
        val recipe = ShapedRecipe(recipeLedMatrix, createLedMatrix())
        recipe.shape(
            "GLG",
            "LCL",
            "GLG"
        )
        recipe.setIngredient('G', Material.GLASS)
        recipe.setIngredient('L', Material.GLOWSTONE_DUST)
        recipe.setIngredient('C', Material.COPPER_INGOT)
        return recipe
    }

    fun createReflector(): ItemStack {
        return ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Отражатель", NamedTextColor.GOLD))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "reflector"
                )
                setCustomModelData(2304)
            }
        }
    }

    fun isReflector(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "reflector"
    }

    fun recipeReflector(): ShapedRecipe {
        val recipe = ShapedRecipe(recipeReflector, createReflector())
        recipe.shape(
            "III",
            "G G",
            "III"
        )
        recipe.setIngredient('I', Material.IRON_INGOT)
        recipe.setIngredient('G', Material.GLASS_PANE)
        return recipe
    }

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

    fun createGrowthLamp(): ItemStack {
        return ItemStack(Material.SHROOMLIGHT).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Фитолампа", NamedTextColor.AQUA))
                lore(
                    listOf(
                        Component.text("BreakingBad", NamedTextColor.GRAY)
                    )
                )
                persistentDataContainer.set(
                    itemKey,
                    PersistentDataType.STRING,
                    "growth_lamp"
                )
            }
        }
    }

    fun isGrowthLamp(item: ItemStack?): Boolean {
        return item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == "growth_lamp"
    }

    fun recipeGrowthLamp(): ShapedRecipe {
        val recipe = ShapedRecipe(recipeGrowthLamp, createGrowthLamp())
        recipe.shape(
            "ARA",
            "BLB",
            "ACA"
        )
        recipe.setIngredient('A', RecipeChoice.ExactChoice(createAluminumPlate()))
        recipe.setIngredient('R', RecipeChoice.ExactChoice(createReflector()))
        recipe.setIngredient('B', RecipeChoice.ExactChoice(createElectronicBoard()))
        recipe.setIngredient('L', RecipeChoice.ExactChoice(createLedMatrix()))
        recipe.setIngredient('C', Material.REDSTONE_LAMP)
        return recipe
    }

    fun recipeCannabisDry(): FurnaceRecipe {
        return FurnaceRecipe(
            driedCannabisRecipeKey,
            createDriedCannabis(),
            RecipeChoice.ExactChoice(createCannabis()),
            5f,
            20 * 180
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