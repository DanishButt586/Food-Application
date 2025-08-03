package com.example.foodapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    val imageRes: Int,
    var name: String,
    var rating: Double,
    var price: String,
    var description: String,
    val freeDelivery: Boolean = true,
    var quantity: Int = 1,
    var isFavorite: Boolean = false       // ← new
) : Parcelable {
    fun getPriceInt(): Int {
        return price
            .replace("₨", "")
            .replace("Rs.", "")
            .replace("Rs", "")
            .replace(",", "")
            .trim()
            .toIntOrNull() ?: 0
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Food) return false
        return name == other.name // assume name is unique
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}
