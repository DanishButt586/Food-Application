package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapplication.databinding.ActivityCheckoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import java.util.*

@Suppress("DEPRECATION")
class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var totalAmount = 0.0
    private val CARD_SCAN_REQUEST = 101

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("card_prefs", Context.MODE_PRIVATE)
        totalAmount = CartManager.getTotalAmount().toDouble()
        binding.tvTotalPrice.text = "Total: Rs. $totalAmount"

        binding.imageView4.setOnClickListener {
            onBackPressed()
        }

        binding.rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCard) {
                showCardBottomSheet()
            }
        }

        binding.btnConfirmOrder.setOnClickListener {
            val address = binding.etAddress.text.toString().trim()

            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter delivery address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = binding.rgPaymentMethod.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPaymentMethod = when (selectedId) {
                R.id.rbCard -> "Credit/Debit Card"
                R.id.rbCashOnDelivery -> "Cash on Delivery"
                else -> "Unknown"
            }

            val intent = Intent(this, BillPageActivity::class.java).apply {
                putExtra("deliveryFee", "Free")
                putExtra("discount", CartManager.getDiscount().toString())
                putExtra("totalAmount", CartManager.getTotalAmount().toString())
                putExtra("paymentMethod", selectedPaymentMethod)
            }

            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("InflateParams")
    private fun showCardBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.card_bottom_sheet, null)
        dialog.setContentView(view)

        val etCardHolder = view.findViewById<EditText>(R.id.etCardHolder)
        val etCardNumber = view.findViewById<EditText>(R.id.etCardNumber)
        val etExpiry = view.findViewById<EditText>(R.id.etExpiry)
        val etCVV = view.findViewById<EditText>(R.id.etCvv)
        val imgCardType = view.findViewById<ImageView>(R.id.imgCardType)

        val previewCardNumber = view.findViewById<TextView>(R.id.previewCardNumber)
        val previewCardHolder = view.findViewById<TextView>(R.id.previewCardHolder)
        val previewExpiry = view.findViewById<TextView>(R.id.previewExpiry)

        val btnSaveCard = view.findViewById<Button>(R.id.btnSaveCard)

        etCardHolder.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                previewCardHolder.text = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                etCardNumber.removeTextChangedListener(this)

                val digits = s.toString().filter { it.isDigit() }.take(16)
                val formatted = digits.chunked(4).joinToString(" ")
                etCardNumber.setText(formatted)
                etCardNumber.setSelection(formatted.length)

                previewCardNumber.text = formatted
                updateCardLogo(digits, imgCardType)

                etCardNumber.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etExpiry.addTextChangedListener(object : TextWatcher {
            var isEditing = false

            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return
                isEditing = true

                val input = s.toString().replace("/", "").filter { it.isDigit() }
                val formatted = when {
                    input.length >= 2 -> input.substring(0, 2) + "/" + input.drop(2).take(2)
                    else -> input
                }

                etExpiry.setText(formatted)
                etExpiry.setSelection(formatted.length)
                previewExpiry.text = formatted

                isEditing = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etCVV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val digits = s.toString().filter { it.isDigit() }
                if (s.toString() != digits) {
                    etCVV.setText(digits)
                    etCVV.setSelection(digits.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnSaveCard.setOnClickListener {
            val cardHolder = etCardHolder.text.toString().trim()
            val cardNumber = etCardNumber.text.toString().filter { it.isDigit() }
            val expiry = etExpiry.text.toString().trim()
            val cvv = etCVV.text.toString().trim()

            if (!validateCard(cardNumber, expiry, cvv)) {
                Toast.makeText(this, "Invalid card details", Toast.LENGTH_SHORT).show()
            } else {
                saveCardLocally(cardHolder, cardNumber, expiry, cvv)
                Toast.makeText(this, "Card Saved Successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun saveCardLocally(holder: String, number: String, expiry: String, cvv: String) {
        with(sharedPreferences.edit()) {
            putString("cardHolder", holder)
            putString("cardNumber", number)
            putString("expiry", expiry)
            putString("cvv", cvv)
            apply()
        }
    }

    private fun updateCardLogo(number: String, imageView: ImageView) {
        val resId = when {
            number.startsWith("4") -> R.drawable.visa_logo
            number.startsWith("5") -> R.drawable.mastercard_logo
            else -> R.drawable.ic_credit_card
        }
        imageView.setImageResource(resId)
    }

    private fun validateCard(number: String, expiry: String, cvv: String): Boolean {
        if (number.length != 16 || cvv.length != 3 || !cvv.all { it.isDigit() }) return false
        if (!expiry.matches(Regex("^\\d{2}/\\d{2}$"))) return false

        val parts = expiry.split("/")
        val month = parts[0].toIntOrNull()
        val year = parts[1].toIntOrNull()
        if (month == null || year == null || month !in 1..12) return false

        val current = Calendar.getInstance()
        val expYear = 2000 + year
        val expMonth = month

        return expYear > current.get(Calendar.YEAR) ||
                (expYear == current.get(Calendar.YEAR) && expMonth >= current.get(Calendar.MONTH) + 1)
    }

    private fun scanCard() {
        val scanIntent = Intent(this, CardIOActivity::class.java).apply {
            putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
            putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
        }
        startActivityForResult(scanIntent, CARD_SCAN_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CARD_SCAN_REQUEST && data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
            scanResult?.let {
                Toast.makeText(this, "Scanned: ${it.cardNumber}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
