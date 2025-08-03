package com.example.foodapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class login_page : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // Hardcoded admin credentials
    private val adminCredentials = mapOf(
        "admin1@gmail.com" to "admin123",
        "admin2@gmail.com" to "admin456",
        "admin3@gmail.com" to "admin789"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = findViewById<Button>(R.id.button2)
        val showPasswordCheckbox = findViewById<CheckBox>(R.id.checkBox)
        val registerText = findViewById<TextView>(R.id.textView8)
        val forgetPasswordText = findViewById<TextView>(R.id.textView5)

        showPasswordCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        loginButton.setOnClickListener {
            val inputEmail = emailEditText.text.toString().trim()
            val inputPassword = passwordEditText.text.toString().trim()

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Check for admin credentials first
            if (adminCredentials.containsKey(inputEmail) && adminCredentials[inputEmail] == inputPassword) {
                Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, admin_page::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            // ✅ Firebase authentication fallback
            auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, dashboard::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        registerText.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        }

        forgetPasswordText.setOnClickListener {
            val intent = Intent(this, forgotpassword::class.java)
            startActivity(intent)
        }
    }
}
