package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.helper.CropStorage
import dev.chsr.breakingBad.items.manager.CustomItemsManager
import dev.chsr.breakingBad.helper.GrowthLampManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class GrowthLampListener(
    private val customItemsManager: CustomItemsManager,
    private val lampManager: GrowthLampManager,
    private val cropStorage: CropStorage
) : Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (!customItemsManager.growthLamp.equals(event.itemInHand)) return

        lampManager.add(event.block.location)
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block
        if (!lampManager.isLamp(block.location)) return

        lampManager.remove(block.location)
        event.isDropItems = false
        block.world.dropItemNaturally(block.location, customItemsManager.growthLamp.create())
    }

    @EventHandler
    fun onDrop(event: BlockDropItemEvent) {
        val block = event.block
        if (!lampManager.hasLampNear(block.location, 4)) return

        val shouldBoost =
            cropStorage.isCannabisCrop(block.location) ||
                    isPlantBlock(block.type)

        if (!shouldBoost) return

        val extraDrops = mutableListOf<ItemStack>()

        for (itemEntity in event.items) {
            val original = itemEntity.itemStack
            val extraAmount = Random.nextInt(1, 3)

            val extra = original.clone()
            extra.amount = extraAmount
            extraDrops.add(extra)
        }

        for (drop in extraDrops) {
            block.world.dropItemNaturally(block.location, drop)
        }
    }

    private fun isPlantBlock(type: Material): Boolean {
        return when (type) {
            Material.WHEAT,
            Material.BEETROOTS,
            Material.CARROTS,
            Material.POTATOES,
            Material.NETHER_WART,
            Material.COCOA,
            Material.SUGAR_CANE,
            Material.CACTUS,
            Material.SWEET_BERRY_BUSH -> true
            else -> false
        }
    }
}