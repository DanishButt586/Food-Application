package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment

class admin_page : AppCompatActivity() {

    private lateinit var pillBurger: LinearLayout
    private lateinit var pillSandwich: LinearLayout
    private lateinit var pillDrink: LinearLayout
    private lateinit var pillPizza: LinearLayout
    private lateinit var pillDessert: LinearLayout
    private lateinit var pillSteak: LinearLayout
    private lateinit var adminWelcomeText: TextView
    private lateinit var navSignOut: LinearLayout

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)


        val fabAddProduct = findViewById<FloatingActionButton>(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        // Initialize views
        pillBurger = findViewById(R.id.pillBurger)
        pillSandwich = findViewById(R.id.pillSandwich)
        pillDrink = findViewById(R.id.pillDrink)
        pillPizza = findViewById(R.id.pillPizza)
        pillDessert = findViewById(R.id.pillDessert)
        pillSteak = findViewById(R.id.pillSteak)
        adminWelcomeText = findViewById(R.id.adminWelcomeText)
        navSignOut = findViewById(R.id.navsignout)

        adminWelcomeText.text = "Welcome Admin!"

        setupCategoryPills()
        setupSignOut()

        // Load default fragment
        loadFragment(BurgerFragment())
        highlightSelected(pillBurger)
    }

    private fun setupSignOut() {
        navSignOut.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes") { _, _ ->
                    signOutUser()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun signOutUser() {
        val intent = Intent(this, login_page::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    private fun setupCategoryPills() {
        val pills = listOf(pillBurger, pillSandwich, pillDrink, pillPizza, pillDessert, pillSteak)

        pills.forEach { pill ->
            pill.setOnClickListener {
                highlightSelected(pill)
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
        val allPills = listOf(pillBurger, pillSandwich, pillDrink, pillPizza, pillDessert, pillSteak)

        allPills.forEach { pill ->
            pill.setBackgroundResource(android.R.color.transparent)
            (pill.getChildAt(1) as? TextView)?.setTextColor(
                ContextCompat.getColor(this, R.color.black)
            )
        }

        selected.setBackgroundResource(R.drawable.bg_onboard_card)
        (selected.getChildAt(1) as? TextView)?.setTextColor(
            ContextCompat.getColor(this, android.R.color.white)
        )
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}