package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = findViewById<EditText>(R.id.editTextText)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val termsCheckbox = findViewById<CheckBox>(R.id.checkBox)
        val registerButton = findViewById<Button>(R.id.button2)
        val loginRedirect = findViewById<TextView>(R.id.textView8)
        val termsText = findViewById<TextView>(R.id.textView5)
        val privacyText = findViewById<TextView>(R.id.textView11)
        val showPasswordCheckBox = findViewById<CheckBox>(R.id.showPasswordCheckBox)

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            password.inputType = if (isChecked) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            password.setSelection(password.text.length)
        }

        registerButton.setOnClickListener {
            val user = username.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()
            val agreedToTerms = termsCheckbox.isChecked

            when {
                user.isEmpty() -> showToast("Username is required")
                userEmail.isEmpty() -> showToast("Email is required")
                !userEmail.contains("@") -> showToast("Invalid email address")
                userPassword.length < 4 -> showToast("Password must be at least 4 characters")
                !agreedToTerms -> showToast("You must agree to the terms")
                else -> registerUser(user, userEmail, userPassword)
            }
        }

        termsText.setOnClickListener {
            val intent = Intent(this, policy::class.java)
            intent.putExtra("type", "terms")
            startActivity(intent)
        }

        privacyText.setOnClickListener {
            val intent = Intent(this, policy::class.java)
            intent.putExtra("type", "privacy")
            startActivity(intent)
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, login_page::class.java))
            finish()
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userMap = hashMapOf(
                        "username" to username,
                        "email" to email
                    )
                    db.collection("users").document(auth.currentUser!!.uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            // ✅ Added Toast message here
                            Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()

                            AlertDialog.Builder(this)
                                .setTitle("Success")
                                .setMessage("User registered successfully!")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(this, login_page::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to save user info", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
