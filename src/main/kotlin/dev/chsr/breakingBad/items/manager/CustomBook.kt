package dev.chsr.breakingBad.items.manager

import org.bukkit.inventory.ItemStack

interface CustomBook {
    fun createBook(): ItemStack
}