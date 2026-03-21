package dev.chsr.breakingBad.items.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

abstract class CustomItem(
    protected val plugin: JavaPlugin,
    val id: String,
    val material: Material,
    val name: String,
    val color: NamedTextColor,
    val customModelData: Int? = null
) {
    private val itemKey = NamespacedKey(plugin, "custom_item_id")
    fun setCmd(item: ItemStack, value: Int) {
        val meta = item.itemMeta ?: return
        val component = meta.customModelDataComponent
        component.floats = listOf(value.toFloat())
        meta.setCustomModelDataComponent(component)
        item.itemMeta = meta
    }

    fun create(): ItemStack = ItemStack(material).apply {
        itemMeta = itemMeta?.apply {
            displayName(Component.text(name, color))
            lore(listOf(Component.text("BreakingBad", NamedTextColor.GRAY)))
            persistentDataContainer.set(itemKey, PersistentDataType.STRING, id)
        }
        setCmd(this, customModelData ?: return@apply)
    }

    fun equals(item: ItemStack?): Boolean =
        item?.itemMeta
            ?.persistentDataContainer
            ?.get(itemKey, PersistentDataType.STRING) == id
}