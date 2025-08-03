package com.example.foodapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class EditFoodActivity : AppCompatActivity() {
    private lateinit var originalFood: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)

        // Get food from repository using imageRes
        val foodFromIntent = intent.getParcelableExtra<Food>("food")!!
        originalFood = FoodRepository.getAllFoods()
            .first { it.imageRes == foodFromIntent.imageRes }

        // Initialize UI
        findViewById<ImageView>(R.id.ivFoodPreview).setImageResource(originalFood.imageRes)
        findViewById<EditText>(R.id.etFoodName).setText(originalFood.name)
        findViewById<EditText>(R.id.etPrice).setText(originalFood.price.replace("Rs.", "").trim())
        findViewById<EditText>(R.id.etRating).setText(originalFood.rating.toString())
        findViewById<EditText>(R.id.etDescription).setText(originalFood.description)

        findViewById<Button>(R.id.btnSaveChanges).setOnClickListener {
            val updatedFood = originalFood.copy(
                name = findViewById<EditText>(R.id.etFoodName).text.toString(),
                price = "Rs. " + findViewById<EditText>(R.id.etPrice).text.toString(),
                rating = findViewById<EditText>(R.id.etRating).text.toString().toDoubleOrNull() ?: 0.0,
                description = findViewById<EditText>(R.id.etDescription).text.toString()
            )

            FoodRepository.updateFoodItem(updatedFood)
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}