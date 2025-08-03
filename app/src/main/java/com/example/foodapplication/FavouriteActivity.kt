package com.example.foodapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavouriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favourite)

        // Back button
        findViewById<ImageView>(R.id.imageView4).setOnClickListener {
            finish()
        }

        setupNavigation()

        findViewById<TextView>(R.id.fvfood).text = "My Favourite Food"

        val rv = findViewById<RecyclerView>(R.id.rvFavorites)
        rv.layoutManager = GridLayoutManager(this, 2)

        // **Deduplicated favorites:**
        val allFoods = FoodRepository.getAllFoods()
        val favItems = allFoods
            .filter { FavoriteManager.isFavorite(it) }
            .distinctBy { it.name }

        rv.adapter = FoodAdapter(favItems) { selectedFood ->
            startActivity(
                Intent(this, activity_food_detail::class.java)
                    .putExtra("SELECTED_FOOD", selectedFood)
            )
        }
    }
    private fun setupNavigation() {
        findViewById<LinearLayout>(R.id.navcart).setOnClickListener {
            if (CartManager.getCartItems().isEmpty()) {
                startActivity(Intent(this, mycartempty::class.java))
            } else {
                startActivity(Intent(this, MyCartActivity::class.java))
            }
        }

        findViewById<LinearLayout>(R.id.navhome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
        }


        findViewById<LinearLayout>(R.id.navprofile).setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }
    }
}
