package solutions.vjk.catalist.infrastructure

import solutions.vjk.catalist.models.Item
import java.util.*

class ItemGenerator {
    companion object {
        private val items = arrayOf(
            "Collard",
            "Greens",
            "Cream",
            "Cheese",
            "Endive",
            "Herring",
            "Ketchup",
            "Kiwi",
            "Lychees",
            "Mint",
            "Pineapple",
            "Prunes",
            "Pumpkin",
            "Rice",
            "Spaghetti",
            "Squash",
            "Calamari",
            "Chorizo",
            "Vinegar",
            "Pie",
            "Apple",
            "Vita-Wheat",
            "Potato",
            "Shrimp",
            "Coke",
            "Mango",
            "Banana",
            "Smarties",
            "Pizza",
            "Bagel",
            "Marinara",
            "Biscuits",
            "Hot Dogs",
            "Popcorn",
            "Calzone",
            "Persimmon",
            "Udon",
            "Pho",
            "Pico de Gallo",
            "Latte",
            "Caramel",
            "Noodles",
            "Pasta",
            "Cupcakes",
            "Salad",
            "Steak",
            "Roast",
            "Beef",
            "Pork",
            "Chicken",
            "Tofu",
            "Soup",
            "Bread",
            "Rye",
            "Wheat",
            "Sourdough",
            "Multigrain",
            "Vegetable",
            "Cucumber",
            "Onion",
            "Leek",
            "Garlic",
            "Mustard",
            "Soy",
            "Cheddar",
            "Ziti",
            "Lasagne",
            "Stuffed",
            "Mushroom",
            "Cauliflower",
            "Orange",
            "Lemon",
            "Strawberry",
            "Cherry",
            "Blueberry",
            "Raspberry",
            "Blackberry",
            "Grapefruit",
        )

        fun generate(count: Int, minCategory: Int, maxCategory: Int, listId: Int): List<Item> {
            val random = Random(Calendar.getInstance().timeInMillis)
            val ret = ArrayList<Item>()
            for (i in 1..count) {
                val ix1 = random.nextInt(items.size)
                var ix2 = random.nextInt(items.size)
                while (ix2 == ix1) {
                    ix2 = random.nextInt(items.size)
                }
                val cat = random.nextInt(maxCategory - minCategory + 1) + minCategory
                ret.add(
                    Item(
                        id = i,
                        categoryId = cat,
                        listId = listId,
                        name = "${items[ix1]} ${items[ix2]}"
                    )
                )
            }
            return ret.toList()
        }
    }
}