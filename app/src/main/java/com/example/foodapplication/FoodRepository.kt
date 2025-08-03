package com.example.foodapplication

object FoodRepository {
    private val allFoods = mutableListOf<Food>()

    fun getAllFoods(): List<Food> = allFoods.toList()

    fun addFoods(newFoods: List<Food>) {
        newFoods.forEach { newFood ->
            if (!allFoods.any { it.name.equals(newFood.name, ignoreCase = true) }) {
                allFoods.add(newFood)
            }
        }
    } // <-- CLOSE addFoods properly here!

    fun updateFoodItem(updatedFood: Food) {
        val index = allFoods.indexOfFirst { it.imageRes == updatedFood.imageRes }
        if (index != -1) {
            allFoods[index] = allFoods[index].copy(
                name = updatedFood.name,
                rating = updatedFood.rating,
                price = updatedFood.price,
                description = updatedFood.description,
                isFavorite = updatedFood.isFavorite
            )
        }
    }

    fun deleteFoodItem(foodToDelete: Food) {
        allFoods.removeAll { it.imageRes == foodToDelete.imageRes }
    }
}
