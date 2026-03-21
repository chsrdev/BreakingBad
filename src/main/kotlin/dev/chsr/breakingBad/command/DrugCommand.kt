package dev.chsr.breakingBad.command

import dev.chsr.breakingBad.items.manager.CustomBook
import dev.chsr.breakingBad.items.manager.CustomItemsManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class DrugCommand(customItemsManager: CustomItemsManager) : TabExecutor {
    val drugMap = hashMapOf(
        Pair("cannabis_seed", customItemsManager.cannabisSeed),
        Pair("cannabis", customItemsManager.cannabis),
        Pair("dried_cannabis", customItemsManager.driedCannabis),
        Pair("joint", customItemsManager.joint),
        Pair("lamp", customItemsManager.growthLamp),
        Pair("aluminum_plate", customItemsManager.aluminumPlate),
        Pair("electronic_board", customItemsManager.electronicBoard),
        Pair("led_matrix", customItemsManager.ledMatrix),
        Pair("reflector", customItemsManager.reflector),
        Pair("chocolate_bar", customItemsManager.chocolateBar),
        Pair("chocolate_bar_with_berries", customItemsManager.chocolateBarWithBerries),
        Pair("chocolate_bar_with_honey", customItemsManager.chocolateBarWithHoney),
        Pair("drug_dealer_book", customItemsManager.drugDealerBook),
        Pair("farmer_recipe_book", customItemsManager.farmerRecipeBook),
        Pair("energy_drink", customItemsManager.energyDrink),
        Pair("energy_mix", customItemsManager.energyMix),
        Pair("medical_capsule", customItemsManager.medicalCapsule),
        Pair("pill_base", customItemsManager.pillBase)
    )

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = sender as? Player ?: return false
        val key = args.getOrNull(0) ?: return false
        val item = drugMap.getOrElse(key) { null } ?: return false
        val itemStack = if (item is CustomBook) item.createBook() else item.create()
        player.inventory.addItem(itemStack)

        player.sendMessage(Component.text("Вы получили ", NamedTextColor.GRAY).append(itemStack.displayName()))
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String?> {
        return drugMap.keys.toList().filter { it.contains(args[0])}
    }
}