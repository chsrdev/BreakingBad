package dev.chsr.breakingBad.addiction

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.UUID

class AddictionStorage(private val plugin: JavaPlugin) {

    data class AddictionData(
        var addiction: Int = 0,
        var lastUse: Long = 0L,
        var lastWithdrawalApply: Long = 0L
    )

    private val data = mutableMapOf<UUID, AddictionData>()

    private val file = File(plugin.dataFolder, "addiction.yml")
    private val config = YamlConfiguration()

    init {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
        load()
    }

    fun get(playerId: UUID): AddictionData {
        return data.getOrPut(playerId) { AddictionData() }
    }

    fun setAddiction(playerId: UUID, amount: Int) {
        val entry = get(playerId)
        entry.addiction = amount.coerceIn(0, 100)
    }

    fun addAddiction(playerId: UUID, amount: Int) {
        val entry = get(playerId)
        entry.addiction = (entry.addiction + amount).coerceIn(0, 100)
        entry.lastUse = System.currentTimeMillis()
        entry.lastWithdrawalApply = 0L
    }

    fun reduceAddiction(playerId: UUID, amount: Int) {
        val entry = get(playerId)
        entry.addiction = (entry.addiction - amount).coerceAtLeast(0)
    }

    fun save() {
        config.set("players", null)

        for ((uuid, entry) in data) {
            config.set("players.$uuid.addiction", entry.addiction)
            config.set("players.$uuid.lastUse", entry.lastUse)
            config.set("players.$uuid.lastWithdrawalApply", entry.lastWithdrawalApply)
        }

        try {
            config.save(file)
        } catch (e: IOException) {
            plugin.logger.severe("Не удалось сохранить addiction.yml")
            e.printStackTrace()
        }
    }

    fun load() {
        data.clear()

        if (!file.exists()) {
            save()
            return
        }

        config.load(file)

        val section = config.getConfigurationSection("players") ?: return
        for (key in section.getKeys(false)) {
            val uuid = runCatching { UUID.fromString(key) }.getOrNull() ?: continue
            val addiction = config.getInt("players.$key.addiction", 0)
            val lastUse = config.getLong("players.$key.lastUse", 0L)
            val lastWithdrawalApply = config.getLong("players.$key.lastWithdrawalApply", 0L)

            data[uuid] = AddictionData(
                addiction = addiction,
                lastUse = lastUse,
                lastWithdrawalApply = lastWithdrawalApply
            )
        }
    }
}