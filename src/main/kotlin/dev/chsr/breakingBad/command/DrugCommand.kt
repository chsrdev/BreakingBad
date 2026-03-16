package dev.chsr.breakingBad.command

import dev.chsr.breakingBad.helper.CustomItems
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DrugCommand(private val customItems: CustomItems): TabExecutor {
    val drugMap = hashMapOf(
        Pair("cannabis_seed", customItems.createCannabisSeed()),
        Pair("cannabis", customItems.createCannabis()),
        Pair("dried_cannabis", customItems.createDriedCannabis()),
        Pair("joint", customItems.createJoint()),
    )

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = sender as? Player ?: return false
        val item = drugMap.getOrElse(args[0]) { null } ?: return false
        player.inventory.addItem(item)

        player.sendMessage(Component.text("Вы получили ", NamedTextColor.GRAY).append(item.displayName()))
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?> {
        return mutableListOf("cannabis_seed", "cannabis", "dried_cannabis", "joint")
    }
}