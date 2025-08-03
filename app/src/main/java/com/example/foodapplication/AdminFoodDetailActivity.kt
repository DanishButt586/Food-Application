package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class AdminFoodDetailActivity : AppCompatActivity() {
    private lateinit var food: Food

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_food_detail)

        val receivedFood = intent.getParcelableExtra<Food>("food") ?: run {
            Toast.makeText(this, "Food item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        food = FoodRepository.getAllFoods()
            .firstOrNull { it.imageRes == receivedFood.imageRes } ?: run {
            Toast.makeText(this, "Food item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<ImageView>(R.id.ivFoodImage).setImageResource(food.imageRes)
        findViewById<TextView>(R.id.tvFoodName).text = food.name
        findViewById<TextView>(R.id.tvPrice).text = food.price
        findViewById<TextView>(R.id.tvRating).text = "★ ${food.rating}"
        findViewById<TextView>(R.id.tvDeliveryStatus).text =
            if (food.freeDelivery) "Free Delivery" else "Delivery Charges Apply"
        findViewById<TextView>(R.id.tvDescription).text = food.description

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            Intent(this, EditFoodActivity::class.java).apply {
                putExtra("food", food)
                startActivity(this)
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete ${food.name}?")
                .setMessage("This action cannot be undone!")
                .setPositiveButton("Delete") { _, _ ->
                    FoodRepository.deleteFoodItem(food)
                    Toast.makeText(this, "${food.name} deleted", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}

