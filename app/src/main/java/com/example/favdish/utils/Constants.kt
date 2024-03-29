package com.example.favdish.utils

object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"
    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"
    const val EXTRA_DISH_DETAILS: String = "DishDetails"
    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"

    const val API_ENDPOINT: String = "recipes/random"
    const val API_KEY: String = "apiKey"
    const val LIMIT_LICENSE: String = "limitLicense"
    const val TAGS: String = "tags"
    const val NUMBER: String = "number"
    const val BASE_URL: String = "https://api.spoonacular.com/"
    const val API_KEY_VALUE: String = "c6a3e0fc5ba340738eee3fab1615b8a2"
    const val LIMIT_LICENSE_VALUE: Boolean = true
    const val TAGS_VALUE: String = "vegetarian, dessert"
    const val NUMBER_VALUE: Int = 1

    const val NOTIFICATION_ID = "FavDish_notification_id"
    const val NOTIFICATION_NAME = "FavDish"
    const val NOTIFICATION_CHANNEL = "FavDish_channel_01"

    fun dishTypes(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Breakfast")
        list.add("Lunch")
        list.add("Dinner")
        list.add("Snacks")
        list.add("Salad")
        list.add("Side dish")
        list.add("Dessert")
        list.add("Other")

        return list
    }

    fun dishCategories(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Pizza")
        list.add("BBQ")
        list.add("Pasta")
        list.add("Bakery")
        list.add("Burger")
        list.add("Chicken")
        list.add("Dessert")
        list.add("Juices")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Sandwiches")
        list.add("Wraps")
        list.add("Cafe")
        list.add("Tea & Coffee")
        list.add("Other")

        return list
    }

    fun dishCookingTime(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("45")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")
        list.add("210")

        return list
    }
}