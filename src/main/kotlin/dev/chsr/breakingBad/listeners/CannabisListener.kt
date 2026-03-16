package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.BreakingBad
import dev.chsr.breakingBad.addiction.AddictionManager
import dev.chsr.breakingBad.helper.CustomItems
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

class CannabisListener(
    private val plugin: BreakingBad,
    private val customItems: CustomItems,
    private val addictionManager: AddictionManager
) : Listener {

    private val burningPlayers = mutableSetOf<UUID>()

    @EventHandler
    fun onUse(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return

        val action = event.action
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player
        val item = event.item ?: return

        if (!customItems.isJoint(item)) return
        if (burningPlayers.contains(player.uniqueId)) return

        event.isCancelled = true
        burningPlayers.add(player.uniqueId)

        player.sendMessage(
            Component.text("Ты поджег косячок..", NamedTextColor.GREEN)
        )
        player.world.playSound(player.location, Sound.ITEM_FLINTANDSTEEL_USE, 1f, 0.8f)

        startBurning(player)
    }

    private fun startBurning(player: Player) {
        val totalTicks = 5 * 20
        val period = 5L
        val steps = (totalTicks / period).toInt()

        object : BukkitRunnable() {
            var currentStep = 0

            override fun run() {
                if (!player.isOnline) {
                    burningPlayers.remove(player.uniqueId)
                    cancel()
                    return
                }

                val item = player.inventory.itemInMainHand

                if (!customItems.isJoint(item)) {
                    burningPlayers.remove(player.uniqueId)
                    cancel()
                    return
                }

                val meta = item.itemMeta as Damageable
                val maxDurability = item.type.maxDurability.toInt()

                if (maxDurability <= 0) {
                    burningPlayers.remove(player.uniqueId)
                    cancel()
                    return
                }

                currentStep++

                val newDamage = ((maxDurability - 1).toDouble() * currentStep / steps).toInt()

                if (currentStep >= steps || newDamage >= maxDurability - 1) {
                    player.inventory.setItemInMainHand(null)

                    player.world.playSound(player.location, Sound.ENTITY_BLAZE_SHOOT, 1f, 0.8f)
                    val smokeLoc = player.eyeLocation.clone().add(
                        player.location.direction.normalize().multiply(0.35)
                    )
                    player.world.spawnParticle(
                        Particle.LARGE_SMOKE,
                        smokeLoc,
                        100,
                        0.03, 0.03, 0.03,
                        0.01
                    )

                    val addiction = addictionManager.get(player).addiction

                    addictionManager.clearWithdrawalEffects(player)
                    addictionManager.applySmokingEffects(player)

                    player.foodLevel = minOf(
                        20,
                        player.foodLevel + when {
                            addiction >= 70 -> 2
                            addiction >= 40 -> 3
                            addiction >= 20 -> 4
                            else -> 5
                        }
                    )

                    player.saturation = minOf(
                        20f,
                        player.saturation + when {
                            addiction >= 70 -> 0.5f
                            addiction >= 40 -> 1.0f
                            addiction >= 20 -> 1.5f
                            else -> 2.0f
                        }
                    )

                    val gain = maxOf(1, 6 - addiction / 20)
                    addictionManager.add(player, gain)

                    player.sendMessage(
                        Component.text("Ты скурил косячок", NamedTextColor.DARK_GREEN)
                    )
                    player.sendMessage(
                        Component.text("Зависимость ${addictionManager.get(player).addiction}/100", NamedTextColor.GRAY)
                    )

                    burningPlayers.remove(player.uniqueId)
                    cancel()
                    return
                }

                meta.damage = newDamage
                item.itemMeta = meta
            }
        }.runTaskTimer(plugin, 0L, period)
    }
}