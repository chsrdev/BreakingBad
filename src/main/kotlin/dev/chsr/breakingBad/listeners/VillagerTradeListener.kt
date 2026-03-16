package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.helper.CustomItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.VillagerCareerChangeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class VillagerTradeListener(
    private val plugin: JavaPlugin,
    private val customItems: CustomItems
) : Listener {

    private val jointRollKey = NamespacedKey(plugin, "joint_trade_roll_done")
    private val jointAllowedKey = NamespacedKey(plugin, "joint_trade_allowed")
    private val jointAddedKey = NamespacedKey(plugin, "joint_trade_added")

    private val farmerSeedRollKey = NamespacedKey(plugin, "farmer_seed_trade_roll_done")
    private val farmerSeedAllowedKey = NamespacedKey(plugin, "farmer_seed_trade_allowed")
    private val farmerSeedAddedKey = NamespacedKey(plugin, "farmer_seed_trade_added")

    @EventHandler
    fun onVillagerSpawn(event: EntitySpawnEvent) {
        if (event.entityType != EntityType.VILLAGER) return

        val villager = event.entity as? Villager ?: return

        Bukkit.getScheduler().runTask(plugin, Runnable {
            setupJointBuyer(villager)

            if (villager.profession == Villager.Profession.FARMER) {
                setupFarmerSeedSeller(villager)
            }
        })
    }

    @EventHandler
    fun onCareerChange(event: VillagerCareerChangeEvent) {
        clearData(event.entity)
        Bukkit.getScheduler().runTask(plugin, Runnable {
            setupJointBuyer(event.entity)
        })
        if (event.profession != Villager.Profession.FARMER) return

        val villager = event.entity

        Bukkit.getScheduler().runTask(plugin, Runnable {
            setupFarmerSeedSeller(villager)
        })
    }

    private fun setupJointBuyer(villager: Villager) {
        val pdc = villager.persistentDataContainer
        Bukkit.broadcastMessage("carrer change")

        if (!pdc.has(jointRollKey, PersistentDataType.BYTE)) {
            pdc.set(jointRollKey, PersistentDataType.BYTE, 1)
            val allowed = if (Random.nextDouble() < 0.2) 1.toByte() else 0.toByte()
            pdc.set(jointAllowedKey, PersistentDataType.BYTE, allowed)
        }

        val allowed = pdc.get(jointAllowedKey, PersistentDataType.BYTE)?.toInt() == 1
        val alreadyAdded = pdc.get(jointAddedKey, PersistentDataType.BYTE)?.toInt() == 1

        if (!allowed || alreadyAdded) return

        val recipes = villager.recipes.toMutableList()
        val trade = buyJointTrade()

        if (recipes.none { sameTrade(it, trade) }) {
            recipes.add(trade)
            villager.recipes = recipes
        }

        pdc.set(jointAddedKey, PersistentDataType.BYTE, 1)
    }

    private fun clearData(villager: Villager) {
        val pdc = villager.persistentDataContainer

        pdc.remove(farmerSeedRollKey)
        pdc.remove(farmerSeedAllowedKey)
        pdc.remove(farmerSeedAddedKey)
        pdc.remove(jointRollKey)
        pdc.remove(jointAllowedKey)
        pdc.remove(jointAddedKey)
    }

    private fun setupFarmerSeedSeller(villager: Villager) {
        val pdc = villager.persistentDataContainer

        if (!pdc.has(farmerSeedRollKey, PersistentDataType.BYTE)) {
            pdc.set(farmerSeedRollKey, PersistentDataType.BYTE, 1)
            val allowed = if (Random.nextDouble() < 0.05) 1.toByte() else 0.toByte()
            pdc.set(farmerSeedAllowedKey, PersistentDataType.BYTE, allowed)
        }

        val allowed = pdc.get(farmerSeedAllowedKey, PersistentDataType.BYTE)?.toInt() == 1
        val alreadyAdded = pdc.get(farmerSeedAddedKey, PersistentDataType.BYTE)?.toInt() == 1

        if (!allowed || alreadyAdded) return
        if (villager.profession != Villager.Profession.FARMER) return

        val recipes = villager.recipes.toMutableList()
        val trade = sellCannabisSeedsTrade()

        if (recipes.none { sameTrade(it, trade) }) {
            recipes.add(trade)
            villager.recipes = recipes
        }

        pdc.set(farmerSeedAddedKey, PersistentDataType.BYTE, 1)
    }

    private fun buyJointTrade(): MerchantRecipe {
        val result = ItemStack(Material.EMERALD, Random.nextInt(16, 40))

        return MerchantRecipe(
            result,
            0,
            6,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(customItems.createJoint().clone().apply { amount = 1 })
        }
    }

    private fun sellCannabisSeedsTrade(): MerchantRecipe {
        val amountSeed = Random.nextInt(1, 6)
        val result = customItems.createCannabisSeed().clone().apply { amount = amountSeed }

        return MerchantRecipe(
            result,
            0,
            4,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(ItemStack(Material.EMERALD, amountSeed * Random.nextInt(8, 17)))
        }
    }

    private fun sameTrade(a: MerchantRecipe, b: MerchantRecipe): Boolean {
        if (!a.result.isSimilar(b.result) || a.result.amount != b.result.amount) return false

        val ai = a.ingredients
        val bi = b.ingredients
        if (ai.size != bi.size) return false

        return ai.zip(bi).all { (x, y) ->
            x.isSimilar(y) && x.amount == y.amount
        }
    }
}