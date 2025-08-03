package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.core.content.edit

@Suppress("DEPRECATION")
class dashboard : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST_CODE = 1001
    }

    private lateinit var tvLocation: TextView
    private lateinit var locationIcon: ImageView
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        tvLocation = findViewById(R.id.tv_location)
        locationIcon = findViewById(R.id.ic_location_pin)
        sharedPrefs = getSharedPreferences("food_app_prefs", MODE_PRIVATE)

        setupSearch()
        setupWindowInsets()
        setupNavigation()
        setupLocation()
        setupCategoryPills()
        setupNotifications()

        // Load saved location if available
        val savedLocation = sharedPrefs.getString("saved_location", null)
        if (!savedLocation.isNullOrEmpty()) {
            tvLocation.text = savedLocation
        }

        loadFragment(BurgerFragment())
        highlightSelected(findViewById(R.id.pillBurger))
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

        findViewById<LinearLayout>(R.id.navfavourite).setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navprofile).setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }
    }

    private fun setupLocation() {
        val locationClickListener = View.OnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivityForResult(intent, LOCATION_REQUEST_CODE)
        }

        locationIcon.setOnClickListener(locationClickListener)
        tvLocation.setOnClickListener(locationClickListener)
    }

    @SuppressLint("CutPasteId")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedLocation = data?.getStringExtra("location_name") ?: "Unknown"
            tvLocation.text = selectedLocation

            // Save to SharedPreferences
            sharedPrefs.edit { putString("saved_location", selectedLocation) }
        }
    }

    private fun setupCategoryPills() {
        val pills = listOf(
            findViewById<LinearLayout>(R.id.pillBurger),
            findViewById<LinearLayout>(R.id.pillSandwich),
            findViewById<LinearLayout>(R.id.pillDrink),
            findViewById<LinearLayout>(R.id.pillPizza),
            findViewById<LinearLayout>(R.id.pillDessert),
            findViewById<LinearLayout>(R.id.pillSteak)
        )

        pills.forEach { pill ->
            pill.setOnClickListener {
                highlightSelected(it as LinearLayout)
                when (pill.id) {
                    R.id.pillBurger -> loadFragment(BurgerFragment())
                    R.id.pillSandwich -> loadFragment(SandwichFragment())
                    R.id.pillDrink -> loadFragment(DrinkFragment())
                    R.id.pillPizza -> loadFragment(PizzaFragment())
                    R.id.pillDessert -> loadFragment(DessertFragment())
                    R.id.pillSteak -> loadFragment(SteakFragment())
                }
            }
        }
    }

    private fun highlightSelected(selected: LinearLayout) {
        val allPills = listOf(
            findViewById<LinearLayout>(R.id.pillBurger),
            findViewById<LinearLayout>(R.id.pillSandwich),
            findViewById<LinearLayout>(R.id.pillDrink),
            findViewById<LinearLayout>(R.id.pillPizza),
            findViewById<LinearLayout>(R.id.pillDessert),
            findViewById<LinearLayout>(R.id.pillSteak)
        )

        allPills.forEach { pill ->
            pill.setBackgroundResource(android.R.color.transparent)
            (pill.getChildAt(1) as TextView).setTextColor(
                ContextCompat.getColor(this, R.color.black)
            )
        }

        selected.setBackgroundResource(R.drawable.bg_onboard_card)
        (selected.getChildAt(1) as TextView).setTextColor(
            ContextCompat.getColor(this, android.R.color.white)
        )
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun setupNotifications() {
        findViewById<ImageView>(R.id.ic_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationPage::class.java))
        }
    }

    private fun setupSearch() {
        findViewById<ImageView>(R.id.ic_search).setOnClickListener {
            startActivity(Intent(this, activity_search::class.java))
        }
    }
}
