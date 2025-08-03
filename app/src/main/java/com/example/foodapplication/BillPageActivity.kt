package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class BillPageActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_page)

        // View bindings
        val tvOrderNumber = findViewById<TextView>(R.id.tvOrderNumber)
        val tvDateTime = findViewById<TextView>(R.id.tvDateTime)
        val tvPaymentMethod = findViewById<TextView>(R.id.tvPaymentMethod)
        val tvDeliveryFee = findViewById<TextView>(R.id.tvDeliveryFee)
        val tvDiscount = findViewById<TextView>(R.id.tvDiscount)
        val tvTotalPayment = findViewById<TextView>(R.id.tvTotalPayment)
        val btnBackHome = findViewById<Button>(R.id.btnBackHome)

        // Get intent data
        val deliveryFee = intent.getStringExtra("deliveryFee") ?: "Free"
        val discountStr = intent.getStringExtra("discount") ?: "0.0"
        val totalStr = intent.getStringExtra("totalAmount") ?: "0.0"
        val paymentMethod = intent.getStringExtra("paymentMethod") ?: "N/A"

        // Convert values
        val discount = discountStr.toDoubleOrNull() ?: 0.0
        val totalAmount = totalStr.toDoubleOrNull() ?: 0.0
        val finalAmount = totalAmount - discount

        // Debug logging (optional)
        Log.d("BillPage", "Total: $totalAmount, Discount: $discount")
        Toast.makeText(this, "Total: $totalAmount\nDiscount: $discount", Toast.LENGTH_SHORT).show()

        // Generate order number and date-time
        val orderNo = (10000..99999).random()
        val currentDateTime = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

        // Set values
        tvOrderNumber.text = "Order #: $orderNo"
        tvDateTime.text = "Date & Time: $currentDateTime"
        tvPaymentMethod.text = "Payment Method: $paymentMethod"
        tvDeliveryFee.text = "Delivery Fee: $deliveryFee"
        tvDiscount.text = "Discount: Rs.${String.format("%.2f", discount)}"
        tvTotalPayment.text = "Total Amount: Rs.${String.format("%.2f", finalAmount)}"

        // Back to Home
        btnBackHome.setOnClickListener {
            val intent = Intent(this, dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
