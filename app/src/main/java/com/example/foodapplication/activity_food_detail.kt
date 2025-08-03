package com.example.foodapplication


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapplication.databinding.ActivityFoodDetailBinding


@Suppress("DEPRECATION")
class activity_food_detail : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private var quantity = 1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val food = intent.getParcelableExtra<Food>("SELECTED_FOOD") ?: run {
            Toast.makeText(this, "Food item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        with(binding) {
            ivFoodImage.setImageResource(food.imageRes)
            tvFoodName.text = food.name
            tvPrice.text = "₨${food.getPriceInt()}"
            tvRating.text = "★ ${food.rating}"
            tvDescription.text = food.description
            tvDeliveryStatus.text = if (food.freeDelivery) "Free Delivery" else "Paid Delivery"
        }

        setupQuantityControls()
        setupAddToCartButton(food)
    }

    private fun setupQuantityControls() {
        binding.tvQuantity.text = quantity.toString()
        binding.btnIncrease.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }
        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }
    }

    private fun setupAddToCartButton(food: Food) {
        binding.btnAddToCart.setOnClickListener {
            val foodCopy = food.copy(quantity = quantity)
            CartManager.addToCart(foodCopy)
            Toast.makeText(this, "${food.name} added to cart", Toast.LENGTH_SHORT).show()
            // navigate back to cart if you want:
            // finish()
        }
    }
}
