package com.example.foodapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class mycartempty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mycartempty)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🍔 Find Food Button - Go to Dashboard
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
        }

        findViewById<LinearLayout>(R.id.navfavourite).setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }

        // 🏠 Bottom Nav Home - Also go to Dashboard
        findViewById<LinearLayout>(R.id.navhome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
        }

        findViewById<LinearLayout>(R.id.navprofile).setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }
    }
}