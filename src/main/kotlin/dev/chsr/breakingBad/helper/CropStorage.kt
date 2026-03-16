package dev.chsr.breakingBad.helper

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class CropStorage(private val plugin: JavaPlugin) {

    private val crops = mutableSetOf<String>()

    private val file: File = File(plugin.dataFolder, "crops.yml")
    private val config = YamlConfiguration()

    init {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
        load()
    }

    fun markCannabisCrop(location: Location) {
        crops.add(toKey(location))
    }

    fun isCannabisCrop(location: Location): Boolean {
        return crops.contains(toKey(location))
    }

    fun unmarkCannabisCrop(location: Location) {
        crops.remove(toKey(location))
    }

    fun clear() {
        crops.clear()
    }

    fun save() {
        config.set("crops", crops.toList())

        try {
            config.save(file)
        } catch (e: IOException) {
            plugin.logger.severe("Не удалось сохранить crops.yml")
            e.printStackTrace()
        }
    }

    fun load() {
        crops.clear()

        if (!file.exists()) {
            try {
                file.createNewFile()
                config.save(file)
            } catch (e: IOException) {
                plugin.logger.severe("Не удалось создать crops.yml")
                e.printStackTrace()
            }
            return
        }

        config.load(file)

        val saved = config.getStringList("crops")
        crops.addAll(saved)
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
        return crops.toSet()
    }

    fun getAllLocations(): List<Location> {
        return crops.mapNotNull { fromKey(it) }
    }
}