package dev.chsr.breakingBad.addiction

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AddictionManager(
    private val plugin: JavaPlugin,
    private val storage: AddictionStorage
) {

    fun set(player: Player, amount: Int) {
        storage.setAddiction(player.uniqueId, amount)
    }

    fun add(player: Player, amount: Int) {
        storage.addAddiction(player.uniqueId, amount)
    }

    fun reduce(player: Player, amount: Int) {
        storage.reduceAddiction(player.uniqueId, amount)
    }

    fun get(player: Player): AddictionStorage.AddictionData {
        return storage.get(player.uniqueId)
    }

    fun startTasks() {
        startDecayTask()
        startWithdrawalTask()
    }

    fun getSmokingEffects(player: Player): List<PotionEffect> {
        val addiction = get(player).addiction

        return when {
            addiction >= 70 -> listOf(
                PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 0),
                PotionEffect(PotionEffectType.RESISTANCE, 20 * 20, 0),
                PotionEffect(PotionEffectType.SLOWNESS, 20 * 15, 0),
                PotionEffect(PotionEffectType.HUNGER, 20 * 40, 0)
            )

            addiction >= 40 -> listOf(
                PotionEffect(PotionEffectType.REGENERATION, 20 * 12, 0),
                PotionEffect(PotionEffectType.RESISTANCE, 20 * 25, 0),
                PotionEffect(PotionEffectType.SPEED, 20 * 30, 0),
                PotionEffect(PotionEffectType.HUNGER, 20 * 35, 0),
                PotionEffect(PotionEffectType.NAUSEA, 20 * 4, 0)
            )

            addiction >= 20 -> listOf(
                PotionEffect(PotionEffectType.REGENERATION, 20 * 16, 0),
                PotionEffect(PotionEffectType.RESISTANCE, 20 * 30, 0),
                PotionEffect(PotionEffectType.SPEED, 20 * 45, 1),
                PotionEffect(PotionEffectType.HUNGER, 20 * 30, 0),
                PotionEffect(PotionEffectType.NAUSEA, 20 * 5, 0)
            )

            else -> listOf(
                PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 1),
                PotionEffect(PotionEffectType.RESISTANCE, 20 * 35, 0),
                PotionEffect(PotionEffectType.SPEED, 20 * 60, 2),
                PotionEffect(PotionEffectType.HUNGER, 20 * 25, 0),
                PotionEffect(PotionEffectType.NAUSEA, 20 * 6, 0)
            )
        }
    }

    fun applySmokingEffects(player: Player) {
        getSmokingEffects(player).forEach(player::addPotionEffect)
    }

    private fun startDecayTask() {
        plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            val now = System.currentTimeMillis()

            for (player in Bukkit.getOnlinePlayers()) {
                val data = storage.get(player.uniqueId)
                val addiction = data.addiction
                if (addiction <= 0) continue

                val minutesSinceUse = (now - data.lastUse) / 1000L / 60L

                val decayStartMinutes = getDecayStartMinutes(addiction)
                if (minutesSinceUse < decayStartMinutes) continue

                storage.reduceAddiction(player.uniqueId, getDecayAmount(addiction))
                player.sendMessage(
                    Component.text(
                        "Зависимость ${storage.get(player.uniqueId).addiction}/100",
                        NamedTextColor.GRAY
                    )
                )
            }
        }, 20L * 60L * 5L, 20L * 60L * 5L)
    }

    fun clearWithdrawalEffects(player: Player) {
        player.removePotionEffect(PotionEffectType.SLOWNESS)
        player.removePotionEffect(PotionEffectType.WEAKNESS)
        player.removePotionEffect(PotionEffectType.HUNGER)
        player.removePotionEffect(PotionEffectType.NAUSEA)
        player.removePotionEffect(PotionEffectType.BLINDNESS)
        player.removePotionEffect(PotionEffectType.MINING_FATIGUE)
    }

    private fun startWithdrawalTask() {
        plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            val now = System.currentTimeMillis()

            for (player in Bukkit.getOnlinePlayers()) {
                val data = storage.get(player.uniqueId)
                val addiction = data.addiction

                if (addiction < 20) continue

                val minutesSinceUse = (now - data.lastUse) / 1000L / 60L
                val minutesSinceWithdrawal = if (data.lastWithdrawalApply == 0L) {
                    Long.MAX_VALUE
                } else {
                    (now - data.lastWithdrawalApply) / 1000L / 60L
                }

                val withdrawalStartMinutes = getWithdrawalStartMinutes(addiction)
                if (minutesSinceUse < withdrawalStartMinutes) continue

                if (minutesSinceWithdrawal < 5) continue

                applyWithdrawal(player, addiction)
                data.lastWithdrawalApply = now
            }
        }, 20L * 10L, 20L * 10L)
    }

    private fun applyWithdrawal(player: Player, addiction: Int) {
        when {
            addiction >= 70 -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 20 * 90, 1))
                player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, 20 * 90, 1))
                player.addPotionEffect(PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * 90, 1))
                player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20 * 45, 0))
                player.addPotionEffect(PotionEffect(PotionEffectType.NAUSEA, 20 * 8, 0))

                player.sendMessage(
                    Component.text(
                        "Началась сильная ломка. Покури, чтобы снять симптомы.",
                        NamedTextColor.DARK_GREEN
                    )
                )
            }

            addiction >= 40 -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 20 * 70, 0))
                player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, 20 * 70, 0))
                player.addPotionEffect(PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * 60, 0))
                player.addPotionEffect(PotionEffect(PotionEffectType.NAUSEA, 20 * 5, 0))

                player.sendMessage(
                    Component.text(
                        "Ты начинаешь чувствовать ломку. Косяк снимет эффекты.",
                        NamedTextColor.GREEN
                    )
                )
            }

            else -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 20 * 45, 0))
                player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, 20 * 45, 0))

                player.sendMessage(
                    Component.text(
                        "Чувствуется легкая ломка. Косяк поможет.",
                        NamedTextColor.GREEN
                    )
                )
            }
        }
    }

    private fun getWithdrawalStartMinutes(addiction: Int): Long {
        return when {
            addiction >= 70 -> 18L
            addiction >= 40 -> 25L
            addiction >= 20 -> 35L
            else -> Long.MAX_VALUE
        }
    }

    private fun getDecayStartMinutes(addiction: Int): Long {
        return when {
            addiction >= 70 -> 60L
            addiction >= 40 -> 45L
            addiction >= 20 -> 30L
            else -> 20L
        }
    }

    private fun getDecayAmount(addiction: Int): Int {
        return when {
            addiction >= 70 -> 1
            addiction >= 40 -> 2
            addiction >= 20 -> 3
            else -> 4
        }
    }
}