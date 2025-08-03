package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

@Suppress("DEPRECATION")
class Settings_page : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)

        // ... window insets setup ...

        setupNavigation()
        setupPolicyLinks()
        setupLanguageSelector()
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.imageView4).setOnClickListener { onBackPressed() }
    }

    private fun setupPolicyLinks() {
        fun launchPolicy(type: String) {
            Intent(this, policy::class.java).apply {
                putExtra("type", type)
                startActivity(this)
            }
        }

        val termsViews = listOf(findViewById<TextView>(R.id.tvTerms), findViewById<ImageView>(R.id.arrowTerms))
        val privacyViews = listOf(findViewById<TextView>(R.id.tvPrivacy), findViewById<ImageView>(R.id.arrowPrivacy))

        termsViews.forEach { view -> view.setOnClickListener { launchPolicy("terms") } }
        privacyViews.forEach { view -> view.setOnClickListener { launchPolicy("privacy") } }
    }

    private fun setupLanguageSelector() {
        val languagePopup = findViewById<ConstraintLayout>(R.id.languagePopup)
        val closePopup = findViewById<View>(R.id.closePopup)
        val selectButton = findViewById<Button>(R.id.btnSelectLang)
        val recyclerView = findViewById<RecyclerView>(R.id.langRecycler)

        // Common click listener for closing popup
        val closeAction = View.OnClickListener { languagePopup.visibility = View.GONE }
        closePopup.setOnClickListener(closeAction)
        selectButton.setOnClickListener(closeAction)

        // Language list setup
        val languages = listOf(
            language("English (US)", R.drawable.us_flag, true),
            language("Urdu", R.drawable.pakistan_flag),
            language("French", R.drawable.french_flag),
            language("Arabic", R.drawable.saudia_flag)
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@Settings_page, LinearLayoutManager.HORIZONTAL, false)
            adapter = LanguageAdapter(languages) { /* Handle selection */ }
        }

        findViewById<ImageView>(R.id.arrowLang).setOnClickListener {
            languagePopup.visibility = View.VISIBLE
        }
    }
}