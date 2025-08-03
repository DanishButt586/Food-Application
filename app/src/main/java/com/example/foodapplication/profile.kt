package com.example.foodapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.createBitmap
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class profile : AppCompatActivity() {

    private lateinit var circleImageView: CircleImageView
    private lateinit var cameraIcon: ImageView
    private lateinit var fullscreenPreviewContainer: FrameLayout
    private lateinit var fullscreenProfileImage: CircleImageView
    private lateinit var btnCloseFullscreen: ImageView
    private lateinit var blurBackground: ImageView
    private lateinit var rootView: ViewGroup

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var textViewEmail: TextView
    private lateinit var textViewUsername: TextView

    private var currentImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    circleImageView.setImageURI(it)
                    saveProfileImage(it.toString())
                }
            }
        }

    @SuppressLint("IntentReset", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        textViewEmail = findViewById(R.id.textView15)
        textViewUsername = findViewById(R.id.textView14)

        circleImageView = findViewById(R.id.ovalImage)
        cameraIcon = findViewById(R.id.imageView5)
        fullscreenPreviewContainer = findViewById(R.id.fullscreenPreviewContainer)
        fullscreenProfileImage = findViewById(R.id.fullscreenProfileImage)
        btnCloseFullscreen = findViewById(R.id.btnCloseFullscreen)
        blurBackground = findViewById(R.id.blurBackground)
        rootView = findViewById(R.id.main)

        loadUserData()

        cameraIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        circleImageView.setOnClickListener { showFullscreenImage() }
        btnCloseFullscreen.setOnClickListener { fullscreenPreviewContainer.visibility = View.GONE }

        setupNavigation()
        setupLogout()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val email = document.getString("email") ?: ""
                    val username = document.getString("username") ?: ""
                    val imageUri = document.getString("imageUri")

                    textViewEmail.text = email
                    textViewUsername.text = username

                    if (!imageUri.isNullOrEmpty()) {
                        Glide.with(this).load(Uri.parse(imageUri)).into(circleImageView)
                    } else {
                        circleImageView.setImageResource(R.drawable.profile)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfileImage(imageUri: String) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .update("imageUri", imageUri)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showFullscreenImage() {
        val bitmap = createBitmap(rootView.width, rootView.height)
        val canvas = Canvas(bitmap)
        rootView.draw(canvas)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blur = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
            blurBackground.setRenderEffect(blur)
        }

        blurBackground.setImageBitmap(bitmap)
        fullscreenProfileImage.setImageDrawable(circleImageView.drawable)
        fullscreenPreviewContainer.visibility = View.VISIBLE
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
        findViewById<LinearLayout>(R.id.navhome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
        }

        val personalDataLayout = findViewById<LinearLayout>(R.id.linearLayout)
        val navigateToPersonalData = View.OnClickListener {
            startActivity(Intent(this, PersonalData::class.java))
        }
        personalDataLayout.setOnClickListener(navigateToPersonalData)
        personalDataLayout.findViewById<TextView>(R.id.tvPersonalData).setOnClickListener(navigateToPersonalData)
        personalDataLayout.findViewById<ImageView>(R.id.ivArrowPersonalData).setOnClickListener(navigateToPersonalData)

        val settingsLayout = findViewById<LinearLayout>(R.id.linearLayout2)
        val navigateToSettings = View.OnClickListener {
            startActivity(Intent(this, Settings_page::class.java))
        }
        settingsLayout.setOnClickListener(navigateToSettings)
        settingsLayout.findViewById<TextView>(R.id.setting).setOnClickListener(navigateToSettings)
        settingsLayout.findViewById<ImageView>(R.id.setting_icon).setOnClickListener(navigateToSettings)

        val addAccountLayout = findViewById<LinearLayout>(R.id.linearLayout3)
        addAccountLayout.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupLogout() {
        val logoutOverlay = findViewById<FrameLayout>(R.id.logoutDialogOverlay)
        val signOutButton = findViewById<Button>(R.id.button2)
        val btnCancelLogout = findViewById<Button>(R.id.btnCancelLogout)
        val btnConfirmLogout = findViewById<Button>(R.id.btnConfirmLogout)

        signOutButton.setOnClickListener { logoutOverlay.visibility = FrameLayout.VISIBLE }
        btnCancelLogout.setOnClickListener { logoutOverlay.visibility = FrameLayout.GONE }

        btnConfirmLogout.setOnClickListener {
            logoutOverlay.visibility = FrameLayout.GONE
            auth.signOut()
            val intent = Intent(this, login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
