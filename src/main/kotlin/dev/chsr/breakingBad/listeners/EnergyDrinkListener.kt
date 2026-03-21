package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.items.manager.CustomItemsManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EnergyDrinkListener(
    private val customItemsManager: CustomItemsManager
): Listener {
    @EventHandler
    fun onPotion(event: PlayerItemConsumeEvent) {
        if (!customItemsManager.energyDrink.equals(event.item)) return
        val player = event.player
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 45, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 20 * 30, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, 20 * 20, 0))
    }
}