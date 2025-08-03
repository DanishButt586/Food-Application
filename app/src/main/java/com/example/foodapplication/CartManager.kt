package com.example.foodapplication

object CartManager {
    private val cartItems = mutableListOf<Food>()

    fun addToCart(item: Food) {
        val existing = cartItems.find { it.name == item.name }
        if (existing != null) {
            existing.quantity += item.quantity
        } else {
            cartItems.add(item)
        }
    }

    /** Subtracts one unit; removes if quantity ≤ 1 */
    fun decrementFromCart(item: Food) {
        val existing = cartItems.find { it.name == item.name } ?: return
        if (existing.quantity > 1) {
            existing.quantity -= 1
        } else {
            cartItems.remove(existing)
        }
    }

    /** Removes the entire item regardless of quantity */
    fun removeFromCart(item: Food) {
        cartItems.removeIf { it.name == item.name }
    }

    fun getCartItems(): List<Food> = cartItems

    fun getTotalItems(): Int = cartItems.sumOf { it.quantity }

    fun getSubtotal(): Int = cartItems.sumOf { it.getPriceInt() * it.quantity }

    fun getDiscount(): Int = (getSubtotal() * 0.04).toInt()

    fun getTotalAmount(): Int = getSubtotal() - getDiscount()
}
