package com.example.foodapplication

data class CartItem(
    val imageResId: Int,
    val name: String,
    val price: Int,
    var quantity: Int
)
