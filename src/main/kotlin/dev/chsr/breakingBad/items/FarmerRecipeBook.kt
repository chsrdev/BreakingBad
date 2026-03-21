package dev.chsr.breakingBad.items

import dev.chsr.breakingBad.items.manager.CustomBook
import dev.chsr.breakingBad.items.manager.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class FarmerRecipeBook(plugin: JavaPlugin) : CustomItem(
    plugin = plugin,
    id = "farmer_recipe_book",
    material = Material.WRITTEN_BOOK,
    name = "Домашние рецепты",
    color = NamedTextColor.YELLOW,
), CustomBook {

    override fun createBook(): ItemStack {
        val book = super.create()
        val meta = book.itemMeta as BookMeta

        meta.displayName(Component.text(name, color))
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "custom_item_id"),
            PersistentDataType.STRING,
            id
        )

        meta.title(Component.text("Домашние рецепты"))
        meta.author(Component.text("Старый фермер"))
        meta.generation = BookMeta.Generation.ORIGINAL

        meta.pages(
            page(
                """
                Ничего лишнего. Только полезные смеси, напитки и простые таблетки.
                
                Всё собирается из обычных вещей, если руки у тебя не кривые.
            """
            ),
            page(
                """
                §lЭнергетическая смесь§0
                
                §8Рецепт:
                §0
                _ С _
                Р Г Р
                _ С _
                
                §8Где:
                §0С §8= Сахар
                §cР §8= Редстоун
                §eГ §8= Светопыль
                
                §8Результат: §6Энергетическая смесь
            """
            ),
            page(
                """
                §lЭнергетик§0
                
                §8Рецепт:
                §0
                _ М _
                Р Р Р
                _ Б _
                
                §8Где:
                §6М §8= Энергетич. смесь
                §cР §8= Редстоун
                §0Б §8= Стекл. бутылочка
                
                §8Результат: §bЭнергетик
            """
            ),
            page(
                """
                §lТаблеточная масса§0
                
                §8Рецепт:
                §0
                _ С _
                М К М
                _ С _
                
                §8Где:
                §0С §8= Сахар
                §0М §8= Молоко
                §7К §8= Костная мука
                
                §8Результат: §0Таблеточная масса
            """
            ),
            page(
                """
                §lТаблетка§0
                
                §8Рецепт:
                §0
                _ С _
                Т Л Т
                _ С _
                
                §8Где:
                §0С §8= Сахар
                §0Т §8= Таблеточ. масса
                §6Л §8= Сверк. ломтик арбуза
                
                §8Результат: §0Таблетка
            """
            ),
            page(
                """
                §dШоколадный батончик§0
                
                §8Рецепт:
                §0
                К К К
                М С П
                К К К
                
                §8Где:
                §0К §8= Какао-бобы
                §0М §8= Молоко
                §6С §8= Сахар
                §6П §8= Пшеница
                
                §8Результат: §0Шоколадный батончик
            """
            ),
            page(
                """
                §cШоколадный батончик с ягодами§0
                
                §8Рецепт:
                §0
                К Я К
                М С П
                К Я К
                
                §8Где:
                §0К §8= Какао-бобы
                §0М §8= Молоко
                §6С §8= Сахар
                §6П §8= Пшеница
                §6Я §8= Сладкие ягоды
                
                §8Результат: §0Шоколадный батончик с ягодами
            """
            ),
            page(
                """
                §cШоколадный батончик с медом§0
                
                §8Рецепт:
                §0
                К Б К
                М С П
                К Б К
                
                §8Где:
                §0К §8= Какао-бобы
                §0М §8= Молоко
                §6С §8= Сахар
                §6П §8= Пшеница
                §6Б §8= Бутылочка с мёдом
                
                §8Результат: §0Шоколадный батончик с медом
            """
            ),
            page(
                """
                §lТаблетка§0
                
                §8Рецепт:
                §0
                _ С _
                Т Л Т
                _ С _
                
                §8Где:
                §0С §8= Сахар
                §0Т §8= Таблеточ. масса
                §6Л §8= Сверк. ломтик арбуза
                
                §8Результат: §0Таблетка
            """
            ),
            page(
                """
                §lСовет§0
                
                Энергетик — для бодрости и скорости.
                
                Таблетка — когда надо прийти в себя.
                
                Держи одно в дороге, другое дома.
            """
            ),
            page(
                """
                §lКонец§0
                
                Если не можешь запомнить рецепт — носи книгу с собой.
                
                Бумага дешевле, чем испорченные ингредиенты.
            """
            )
        )

        book.itemMeta = meta
        return book
    }

    private fun page(text: String): Component {
        return Component.text(text.trimIndent())
    }
}