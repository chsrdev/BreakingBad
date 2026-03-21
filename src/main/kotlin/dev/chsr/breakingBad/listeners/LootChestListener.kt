package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.items.manager.CustomItemsManager
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class LootChestListener(
    private val customItemsManager: CustomItemsManager
) : Listener {

    @EventHandler
    fun onLootGenerate(event: LootGenerateEvent) {
        val key = event.lootTable.key

        val drugDealerBook = customItemsManager.drugDealerBook.createBook()
        val farmerRecipeBook = customItemsManager.farmerRecipeBook.createBook()
        val cannabisSeed = customItemsManager.farmerRecipeBook.createBook()

        tryAddLoot(event, drugDealerBook, chanceForDrugDealerBook(key))
        tryAddLoot(event, cannabisSeed, chanceForDrugDealerBook(key))
        tryAddLoot(event, farmerRecipeBook, chanceForFarmerRecipeBook(key))
    }

    private fun tryAddLoot(
        event: LootGenerateEvent,
        item: ItemStack,
        chance: Double?
    ) {
        if (chance == null) return
        if (Random.nextDouble() >= chance) return
        if (event.loot.any { it.isSimilar(item) }) return

        event.loot.add(item)
    }

    private fun chanceForDrugDealerBook(key: NamespacedKey): Double? = when (key) {
        NamespacedKey.minecraft("chests/abandoned_mineshaft") -> 0.18
        NamespacedKey.minecraft("chests/simple_dungeon") -> 0.16
        NamespacedKey.minecraft("chests/stronghold_corridor") -> 0.12
        NamespacedKey.minecraft("chests/stronghold_crossing") -> 0.10
        NamespacedKey.minecraft("chests/woodland_mansion") -> 0.10
        NamespacedKey.minecraft("chests/shipwreck_supply") -> 0.08
        NamespacedKey.minecraft("chests/pillager_outpost") -> 0.07

        NamespacedKey.minecraft("chests/village/village_armorer") -> 0.02
        NamespacedKey.minecraft("chests/village/village_butcher") -> 0.02
        NamespacedKey.minecraft("chests/village/village_cartographer") -> 0.03
        NamespacedKey.minecraft("chests/village/village_desert_house") -> 0.02
        NamespacedKey.minecraft("chests/village/village_fisher") -> 0.02
        NamespacedKey.minecraft("chests/village/village_fletcher") -> 0.02
        NamespacedKey.minecraft("chests/village/village_mason") -> 0.02
        NamespacedKey.minecraft("chests/village/village_plains_house") -> 0.03
        NamespacedKey.minecraft("chests/village/village_savanna_house") -> 0.03
        NamespacedKey.minecraft("chests/village/village_shepherd") -> 0.03
        NamespacedKey.minecraft("chests/village/village_snowy_house") -> 0.03
        NamespacedKey.minecraft("chests/village/village_taiga_house") -> 0.03
        NamespacedKey.minecraft("chests/village/village_tannery") -> 0.03
        NamespacedKey.minecraft("chests/village/village_temple") -> 0.03
        NamespacedKey.minecraft("chests/village/village_toolsmith") -> 0.02
        NamespacedKey.minecraft("chests/village/village_weaponsmith") -> 0.02

        else -> null
    }

    private fun chanceForFarmerRecipeBook(key: NamespacedKey): Double? = when (key) {
        NamespacedKey.minecraft("chests/village/village_armorer") -> 0.06
        NamespacedKey.minecraft("chests/village/village_butcher") -> 0.08
        NamespacedKey.minecraft("chests/village/village_cartographer") -> 0.05
        NamespacedKey.minecraft("chests/village/village_desert_house") -> 0.07
        NamespacedKey.minecraft("chests/village/village_fisher") -> 0.06
        NamespacedKey.minecraft("chests/village/village_fletcher") -> 0.05
        NamespacedKey.minecraft("chests/village/village_mason") -> 0.04
        NamespacedKey.minecraft("chests/village/village_plains_house") -> 0.10
        NamespacedKey.minecraft("chests/village/village_savanna_house") -> 0.09
        NamespacedKey.minecraft("chests/village/village_shepherd") -> 0.06
        NamespacedKey.minecraft("chests/village/village_snowy_house") -> 0.09
        NamespacedKey.minecraft("chests/village/village_taiga_house") -> 0.09
        NamespacedKey.minecraft("chests/village/village_tannery") -> 0.06
        NamespacedKey.minecraft("chests/village/village_temple") -> 0.05
        NamespacedKey.minecraft("chests/village/village_toolsmith") -> 0.04
        NamespacedKey.minecraft("chests/village/village_weaponsmith") -> 0.04

        NamespacedKey.minecraft("chests/shipwreck_supply") -> 0.05
        NamespacedKey.minecraft("chests/abandoned_mineshaft") -> 0.04
        NamespacedKey.minecraft("chests/simple_dungeon") -> 0.03
        NamespacedKey.minecraft("chests/igloo_chest") -> 0.06
        NamespacedKey.minecraft("chests/pillager_outpost") -> 0.03

        else -> null
    }
}