package dev.chsr.breakingBad.helper

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class GrowthLampManager(
    private val plugin: JavaPlugin
) {
    private val lamps = mutableSetOf<String>()

    fun load() {
        lamps.clear()
        lamps.addAll(plugin.config.getStringList("growth-lamps"))
    }

    fun save() {
        plugin.config.set("growth-lamps", lamps.toList())
        plugin.saveConfig()
    }

    fun add(location: Location) {
        lamps.add(toKey(location))
        save()
    }

    fun remove(location: Location) {
        lamps.remove(toKey(location))
        save()
    }

    fun isLamp(location: Location): Boolean {
        return lamps.contains(toKey(location))
    }

    fun getLocations(): List<Location> {
        return lamps.mapNotNull(::fromKey)
    }

    fun hasLampNear(location: Location, radius: Int): Boolean {
        val world = location.world ?: return false

        return lamps.any { key ->
            val lampLoc = fromKey(key) ?: return@any false
            lampLoc.world == world &&
                    kotlin.math.abs(lampLoc.blockX - location.blockX) <= radius &&
                    kotlin.math.abs(lampLoc.blockY - location.blockY) <= radius &&
                    kotlin.math.abs(lampLoc.blockZ - location.blockZ) <= radius
        }
    }

    private fun toKey(location: Location): String {
        return "${location.world?.name};${location.blockX};${location.blockY};${location.blockZ}"
    }

    private fun fromKey(key: String): Location? {
        val parts = key.split(";")
        if (parts.size != 4) return null

        val world = Bukkit.getWorld(parts[0]) ?: return null
        val x = parts[1].toIntOrNull() ?: return null
        val y = parts[2].toIntOrNull() ?: return null
        val z = parts[3].toIntOrNull() ?: return null

        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }
}