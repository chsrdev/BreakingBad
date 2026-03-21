package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class Cannabis(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "cannabis",
    material = Material.SUGAR_CANE,
    name = "Каннабис",
    color = NamedTextColor.GREEN,
    customModelData = 2002
)