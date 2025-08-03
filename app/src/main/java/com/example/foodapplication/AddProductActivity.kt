package com.example.foodapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {

    private val categories = mapOf(
        "Burger" to R.drawable.burger,
        "Sandwich" to R.drawable.sandwich,
        "Pizza" to R.drawable.pizza,
        "Drink" to R.drawable.juice,
        "Dessert" to R.drawable.dessert,
        "Steak" to R.drawable.steak
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val spinner = findViewById<Spinner>(R.id.spCategory)
        val etName = findViewById<EditText>(R.id.etProductName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etRating = findViewById<EditText>(R.id.etRating)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnAdd = findViewById<Button>(R.id.btnAddProduct)

        // Setup spinner with categories
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories.keys.toList()
        )

        btnAdd.setOnClickListener {
            val category = spinner.selectedItem.toString()
            val name = etName.text.toString().trim()
            val priceText = etPrice.text.toString().trim()
            val ratingText = etRating.text.toString().trim()
            val description = etDescription.text.toString().trim()

            val price = priceText.toDoubleOrNull()
            val rating = ratingText.toDoubleOrNull()

            if (validateInputs(name, price, rating, description)) {
                val newFood = Food(
                    imageRes = categories[category] ?: R.drawable.burger6,
                    name = name,
                    rating = rating!!,
                    price = formatPrice(priceText),
                    description = description,
                    freeDelivery = true
                )

                FoodRepository.addFoods(listOf(newFood))
                Toast.makeText(this, "$name added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validateInputs(
        name: String,
        price: Double?,
        rating: Double?,
        description: String
    ): Boolean {
        return when {
            name.isBlank() -> showError("Enter product name")
            price == null || price <= 0 -> showError("Enter valid price")
            rating == null || rating !in 0.0..5.0 -> showError("Rating must be 0-5")
            description.isBlank() -> showError("Enter description")
            else -> true
        }
    }

    private fun formatPrice(priceText: String): String {
        return if (priceText.toDoubleOrNull() != null) "Rs. $priceText"
        else "Rs. 0"
    }

    private fun showError(message: String): Boolean {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        return false
    }
}