package com.example.foodapplication

object FavoriteManager {
    private val favorites = mutableSetOf<String>() // Track by food name

    fun toggle(food: Food): Boolean {
        return if (favorites.contains(food.name)) {
            favorites.remove(food.name)
            false
        } else {
            favorites.add(food.name)
            true
        }
    }

    fun isFavorite(food: Food): Boolean {
        return favorites.contains(food.name)
    }

    fun all(): List<String> = favorites.toList()
}
