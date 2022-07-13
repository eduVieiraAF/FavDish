package com.example.favdish.utils

object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

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

    fun  dishCategories(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Pizza")
        list.add("BBQ")
        list.add("Bakery")
        list.add("Burger")
        list.add("Cafe")
        list.add("Chicken")
        list.add("Dessert")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Juices")
        list.add("Sandwiches")
        list.add("Tea & Coffee")
        list.add("Wraps")
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

        return list
    }
}