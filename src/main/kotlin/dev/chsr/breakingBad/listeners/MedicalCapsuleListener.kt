package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.items.manager.CustomItemsManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MedicalCapsuleListener(
    private val customItemsManager: CustomItemsManager
) : Listener{
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return

        val action = event.action
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player
        val item = event.item ?: return
        if (!customItemsManager.medicalCapsule.equals(item)) return

        player.inventory.remove(item)
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 20 * 20, 0))
        player.removePotionEffect(PotionEffectType.NAUSEA)
        player.removePotionEffect(PotionEffectType.WEAKNESS)
        player.removePotionEffect(PotionEffectType.SLOWNESS)
    }
}