package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class CannabisSeed(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "cannabis_seed",
    material = Material.BEETROOT_SEEDS,
    name = "Семена каннабиса",
    color = NamedTextColor.DARK_GREEN,
    customModelData = 2001
)