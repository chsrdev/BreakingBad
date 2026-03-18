package me.example.myplugin

import dev.chsr.breakingBad.helper.CropStorage
import dev.chsr.breakingBad.helper.CustomItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockGrowEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import kotlin.random.Random

class CannabisSeedListener(
    private val customItems: CustomItems,
    private val cropStorage: CropStorage
) : Listener {
    @EventHandler
    fun onPlant(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock == null) return

        val clicked = event.clickedBlock
        val item = event.item

        if (clicked!!.type != Material.FARMLAND) return
        if (!customItems.isCannabisSeed(item)) return

        val plantBlock = clicked.getRelative(0, 1, 0)
        if (!plantBlock.type.isAir()) return

        plantBlock.type = Material.WHEAT

        if (plantBlock.blockData is Ageable) {
            val blockData = plantBlock.blockData as Ageable
            blockData.age = 0
            plantBlock.blockData = blockData
        }

        cropStorage.markCannabisCrop(plantBlock.location)

        item?.amount = item.amount - 1

        event.setCancelled(true)
    }

    @EventHandler
    fun onGrow(event: BlockGrowEvent) {
        if (cropStorage.isCannabisCrop(event.getBlock().location)) {
            val r = Random.nextInt(0, 3) // 33% chance, takes near 1h 30m for full growth
            event.isCancelled = r != 1
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return

        val action = event.action
        if (action == Action.RIGHT_CLICK_BLOCK
            && cropStorage.isCannabisCrop(event.clickedBlock!!.location)
            && event.item?.type == Material.BONE_MEAL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block

        if (!cropStorage.isCannabisCrop(block.location)) {
            return;
        }

        event.isDropItems = false

        var mature = false;
        if (block.blockData is Ageable) {
            val ageable = block.blockData as Ageable
            mature = ageable.age >= ageable.maximumAge
        }

        block.type = Material.AIR;
        cropStorage.unmarkCannabisCrop(block.location)

        val seed = customItems.createCannabisSeed()
        if (mature) {
            val cannabis = customItems.createCannabis()
            cannabis.amount = Random.nextInt(4, 9)
            seed.amount = Random.nextInt(0, 2)
            block.world.dropItemNaturally(block.location, cannabis)
            block.world.dropItemNaturally(block.location, seed)
        } else {
            block.world.dropItemNaturally(block.location, seed)
        }
    }
}