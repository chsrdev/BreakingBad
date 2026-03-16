package dev.chsr.breakingBad.command

import dev.chsr.breakingBad.addiction.AddictionManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class AddictionCommand(private val addictionManager: AddictionManager) : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?> {
        return mutableListOf("set", "add", "reduce")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = sender as? Player ?: return false
        if (args.size < 2) return false
        when (args[0]) {
            "set" -> {
                val input = args[1].toIntOrNull()
                if (input != null)
                    addictionManager.set(player, input)
            }
            "add" -> addictionManager.add(player, args[1].toIntOrNull() ?: 0)
            "reduce" -> addictionManager.reduce(player, args[1].toIntOrNull() ?: 0)
        }

        sender.sendMessage(
            Component.text(
                "Зависимость ${player.name}: ${addictionManager.get(player).addiction}/100",
                NamedTextColor.GRAY
            )
        )
        return true
    }
}