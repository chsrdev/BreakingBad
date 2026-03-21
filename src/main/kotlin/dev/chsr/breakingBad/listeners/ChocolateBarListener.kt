package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.items.manager.CustomItemsManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ChocolateBarListener(
    private val plugin: JavaPlugin,
    private val customItemsManager: CustomItemsManager
) : Listener {
    @EventHandler
    fun onConsume(event: PlayerItemConsumeEvent) {
        event.player.addPotionEffect(
            when {
                customItemsManager.chocolateBar.equals(event.item) -> {
                    PotionEffect(
                        PotionEffectType.SPEED,
                        20 * 30,
                        1
                    )
                }
                customItemsManager.chocolateBarWithBerries.equals(event.item) -> {
                    PotionEffect(
                        PotionEffectType.REGENERATION,
                        20 * 45,
                        1
                    )
            }
                customItemsManager.chocolateBarWithHoney.equals(event.item) -> {
                    PotionEffect(
                        PotionEffectType.RESISTANCE,
                        20 * 60,
                        1
                    )
                }
                else -> return
        }
        )
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        val result = event.recipe.result

        if (customItemsManager.chocolateBar.equals(result)
            || customItemsManager.chocolateBarWithHoney.equals(result)
            || customItemsManager.chocolateBarWithBerries.equals(result)) {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                val player = event.whoClicked as? Player ?: return@Runnable
                var bucketsUsed = 0
                event.inventory.matrix.forEach { item ->
                    if (item?.type == Material.MILK_BUCKET) {
                        bucketsUsed += item.amount
                    }
                }

                repeat(bucketsUsed) {
                    event.inventory.addItem(ItemStack(Material.BUCKET))
                }
            })
        }
    }
}