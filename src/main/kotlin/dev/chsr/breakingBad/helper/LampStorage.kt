package dev.chsr.breakingBad.helper

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class LampStorage(private val plugin: JavaPlugin) {

    private val lamps = mutableSetOf<String>()

    private val file: File = File(plugin.dataFolder, "lamps.yml")
    private val config = YamlConfiguration()

    init {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
        load()
    }

    fun markLamp(location: Location) {
        lamps.add(toKey(location))
    }

    fun isLamp(location: Location): Boolean {
        return lamps.contains(toKey(location))
    }

    fun unmarkLamp(location: Location) {
        lamps.remove(toKey(location))
    }

    fun clear() {
        lamps.clear()
    }

    fun save() {
        config.set("lamps", lamps.toList())

        try {
            config.save(file)
        } catch (e: IOException) {
            plugin.logger.severe("Не удалось сохранить lamps.yml")
            e.printStackTrace()
        }
    }

    fun load() {
        lamps.clear()

        if (!file.exists()) {
            try {
                file.createNewFile()
                config.save(file)
            } catch (e: IOException) {
                plugin.logger.severe("Не удалось создать lamps.yml")
                e.printStackTrace()
            }
            return
        }

        config.load(file)

        val saved = config.getStringList("lamps")
        lamps.addAll(saved)
    }

    private fun toKey(location: Location): String {
        val worldName = location.world?.name ?: "null"
        return "$worldName;${location.blockX};${location.blockY};${location.blockZ}"
    }

    fun fromKey(key: String): Location? {
        val parts = key.split(";")
        if (parts.size != 4) return null

        val world = Bukkit.getWorld(parts[0]) ?: return null
        val x = parts[1].toIntOrNull() ?: return null
        val y = parts[2].toIntOrNull() ?: return null
        val z = parts[3].toIntOrNull() ?: return null

        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun getAllKeys(): Set<String> {
        return lamps.toSet()
    }

    fun getAllLocations(): List<Location> {
        return lamps.mapNotNull { fromKey(it) }
    }

    fun hasLampNear(location: Location, radius: Int): Boolean {
        val world = location.world ?: return false

        for (lamp in getAllLocations()) {
            if (lamp.world != world) continue

            if (kotlin.math.abs(lamp.blockX - location.blockX) <= radius &&
                kotlin.math.abs(lamp.blockY - location.blockY) <= radius &&
                kotlin.math.abs(lamp.blockZ - location.blockZ) <= radius
            ) {
                return true
            }
        }

        return false
    }
}