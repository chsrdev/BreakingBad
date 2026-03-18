package dev.chsr.breakingBad.helper

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class GrowthLampTask(
    private val plugin: JavaPlugin,
    private val lampManager: GrowthLampManager,
    private val cropStorage: CropStorage
) {

    fun startTasks() {
        plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            val tried = setOf<Location>()
            for (lamp in lampManager.getLocations()) {
                val world = lamp.world ?: continue

                for (x in -4..4) {
                    for (y in -2..2) {
                        for (z in -4..4) {
                            val block = world.getBlockAt(
                                lamp.blockX + x,
                                lamp.blockY + y,
                                lamp.blockZ + z
                            )
                            tryGrowBlock(block)
                            tried.plus(block.location)
                        }
                    }
                }
            }
        }, 20L * 100L, 20L * 100L)
    }

    private fun tryGrowBlock(block: Block) {
        if (cropStorage.isCannabisCrop(block.location)) {
            tryGrowCannabis(block)
            return
        }

        val data = block.blockData
        if (data is Ageable) {
            if (data.age < data.maximumAge && Random.nextDouble() < 0.45) {
                data.age = minOf(data.maximumAge, data.age + 1)
                block.blockData = data
            }
            return
        }

        when (block.type) {
            Material.SUGAR_CANE,
            Material.CACTUS -> {
                if (Random.nextDouble() < 0.20) {
                    tryGrowVertical(block)
                }
            }
            else -> {}
        }
    }

    private fun tryGrowCannabis(block: Block) {
        val data = block.blockData as? Ageable ?: return
        if (data.age >= data.maximumAge) return

        val success = Random.nextInt(0, 3) == 1
        if (!success) return

        data.age = minOf(data.maximumAge, data.age + 1)
        block.blockData = data
    }

    private fun tryGrowVertical(block: Block) {
        val above = block.getRelative(0, 1, 0)
        if (!above.type.isAir) return

        var height = 1
        var current = block.getRelative(0, -1, 0)

        while (current.type == block.type) {
            height++
            current = current.getRelative(0, -1, 0)
        }

        if (height < 3) {
            above.type = block.type
        }
    }
}