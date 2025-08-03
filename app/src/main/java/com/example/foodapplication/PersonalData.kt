package com.example.foodapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

@Suppress("DEPRECATION")
class PersonalData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_data)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backIcon: ImageView = findViewById(R.id.imageView4)
        backIcon.setOnClickListener { onBackPressed() }

        val editFullName = findViewById<EditText>(R.id.editFullName)
        val editDob = findViewById<EditText>(R.id.editDob)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val genderTextView = findViewById<TextView>(R.id.genderSpinner)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Date picker setup
        editDob.setOnClickListener { showDatePicker(editDob) }

        // Gender picker dialog
        val genderOptions = arrayOf("Male", "Female", "Prefer not to say")
        genderTextView.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Select Gender")
                .setItems(genderOptions) { _, which ->
                    genderTextView.text = genderOptions[which]
                }
                .show()
        }

        // Save button validation
        saveButton.setOnClickListener {
            val fullName = editFullName.text.toString().trim()
            val dob = editDob.text.toString().trim()
            val phone = editPhone.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val gender = genderTextView.text.toString().trim()

            var isValid = true

            if (fullName.isEmpty()) {
                editFullName.error = "Full name is required"
                isValid = false
            }

            if (dob.isEmpty()) {
                editDob.error = "Date of birth is required"
                isValid = false
            }

            if (phone.length !in 10..11) {
                editPhone.error = "Enter a valid 10–11 digit phone number"
                isValid = false
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editEmail.error = "Enter a valid email address"
                isValid = false
            }

            if (gender.isEmpty() || gender == "Gender") {
                genderTextView.error = "Please select gender"
                isValid = false
            } else {
                genderTextView.error = null
            }

            if (isValid) {
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                // TODO: Save data here
            }
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val formattedDate = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                editText.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}
