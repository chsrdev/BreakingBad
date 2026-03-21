package dev.chsr.breakingBad.listeners

import dev.chsr.breakingBad.items.manager.CustomItemsManager
import kotlin.random.Random

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin

class DrugDealerListener(
    private val plugin: JavaPlugin,
    private val customItemsManager: CustomItemsManager
) : Listener {
    @EventHandler
    fun onTraderSpawn(event: EntitySpawnEvent) {
        if (event.entityType != EntityType.WANDERING_TRADER) return
        val trader = event.entity as? WanderingTrader ?: return

        Bukkit.getScheduler().runTask(plugin, Runnable {
            val recipes = trader.recipes.toMutableList()

            addIfMissing(recipes, sellCannabisSeeds())
            addIfMissing(recipes, sellJoint())
            addIfMissing(recipes, buyCannabisSeeds())
            addIfMissing(recipes, buyJoint())
            addIfMissing(recipes, sellBook())

            trader.recipes = recipes
        })
    }

    private fun sellCannabisSeeds(): MerchantRecipe {
        val cannabisAmount = Random.nextInt(1, 4)
        val result = customItemsManager.cannabisSeed.create().apply { amount = cannabisAmount }

        return MerchantRecipe(
            result,
            0,
            3,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(ItemStack(Material.EMERALD, cannabisAmount*Random.nextInt(8, 15)))
        }
    }

    private fun sellBook(): MerchantRecipe {
        val result = customItemsManager.drugDealerBook.createBook()

        return MerchantRecipe(
            result,
            0,
            1,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(ItemStack(Material.EMERALD, Random.nextInt(16, 24)))
        }
    }

    private fun sellJoint(): MerchantRecipe {
        val result = customItemsManager.joint.create()

        return MerchantRecipe(
            result,
            0,
            8,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(ItemStack(Material.EMERALD, Random.nextInt(16, 32)))
        }
    }

    private fun buyCannabisSeeds(): MerchantRecipe {
        val result = ItemStack(Material.EMERALD, Random.nextInt(1, 13))

        return MerchantRecipe(
            result,
            0,
            10,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(customItemsManager.cannabisSeed.create().apply { amount = 1 })
        }
    }

    private fun buyJoint(): MerchantRecipe {
        val result = ItemStack(Material.EMERALD, Random.nextInt(10, 25))

        return MerchantRecipe(
            result,
            0,
            16,
            true,
            0,
            0.05f
        ).apply {
            addIngredient(customItemsManager.joint.create())
        }
    }

    private fun addIfMissing(recipes: MutableList<MerchantRecipe>, recipe: MerchantRecipe) {
        val exists = recipes.any { sameTrade(it, recipe) }
        if (!exists) {
            recipes.add(recipe)
        }
    }

    private fun sameTrade(a: MerchantRecipe, b: MerchantRecipe): Boolean {
        if (!a.result.isSimilar(b.result)) return false

        val aIngredients = a.ingredients
        val bIngredients = b.ingredients

        if (aIngredients.size != bIngredients.size) return false

        return aIngredients.zip(bIngredients).all { (x, y) ->
            x.isSimilar(y) && x.amount == y.amount
        }
    }
}