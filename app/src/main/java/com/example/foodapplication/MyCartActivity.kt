package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapplication.databinding.ActivityMyCartBinding

class MyCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCartBinding
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Nav: Home → Dashboard
        findViewById<LinearLayout>(R.id.navhome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
        }

        findViewById<LinearLayout>(R.id.navfavourite).setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }
        // Bottom Nav: Profile
        findViewById<LinearLayout>(R.id.navprofile).setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }

        // Setup RecyclerView
        adapter = CartAdapter(CartManager.getCartItems().toMutableList()) {
            updatePaymentSummary()
        }
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = adapter

        // Go to Checkout on Continue Button
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("deliveryFee", "Free")
                putExtra("discount", CartManager.getDiscount())
                putExtra("totalAmount", CartManager.getTotalAmount())
            }
            startActivity(intent)
        }

        updatePaymentSummary()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        updatePaymentSummary()
    }

    @SuppressLint("SetTextI18n")
    private fun updatePaymentSummary() {
        val totalItems = CartManager.getTotalItems()
        val discount = CartManager.getDiscount()
        val total = CartManager.getTotalAmount()

        binding.tvTotalItems.text = "Total Items: $totalItems"
        binding.tvDeliveryFee.text = "Delivery Fee: Free"
        binding.tvDiscount.text = "Discount: ₨$discount"
        binding.tvTotalAmount.text = "Total: ₨$total"
    }
}
