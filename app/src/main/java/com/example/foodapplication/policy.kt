package com.example.foodapplication

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class policy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        val policyText = findViewById<TextView>(R.id.policy_text)
        val closeButton = findViewById<ImageButton>(R.id.close_button)
        val type = intent.getStringExtra("type")

        val termsText = """
            Terms and Conditions
            Effective Date: 12/05/2025
            App Name: Foodies ("we", "our", or "us")
            By using our app, you agree to the following Terms and Conditions. If you do not agree, please do not use the app.

            1. Acceptance of Terms
            By accessing or using Foodies, you acknowledge that you have read, understood, and agreed to be bound by these Terms and Conditions, as well as our Privacy Policy.

            2. Eligibility
            You must be at least 18 years old to use this app. By using it, you confirm that you are of legal age and capable of entering into a binding agreement.

            3. User Account
            • You must create an account to use most features.
            • Keep your login credentials secure.
            • You are responsible for all activities that occur under your account.
            • We reserve the right to suspend or terminate accounts for violations of these Terms or any suspicious activity.

            4. Ordering and Payment
            • Orders can be placed from listed restaurants within your delivery area.
            • Prices, availability, and delivery times are subject to change.
            • Payment must be made via accepted methods (e.g., credit/debit cards, digital wallets, or cash on delivery if applicable).
            • We use third-party payment gateways; ensure their terms are acceptable to you before proceeding.

            5. Cancellations and Refunds
            • Orders can only be canceled within a limited time after placing.
            • Refunds are subject to restaurant, delivery, and payment partner policies.
            • Ineligible or fraudulent refund requests may be denied.

            6. Delivery
            • Estimated delivery times are for reference and may vary.
            • Delays due to weather, traffic, or operational issues are possible.
            • Please ensure that your delivery address and contact information are accurate to avoid delivery issues.

            7. User Conduct
            You agree not to:
            • Misuse the app or place false orders
            • Harass or abuse delivery personnel or restaurant staff
            • Violate any laws or third-party rights while using the app

            8. Intellectual Property
            All content in the app (including logos, designs, text, and images) is owned by or licensed to us. You may not copy, modify, or distribute our content without prior written permission.

            9. Limitation of Liability
            We are not liable for:
            • Delays or failures in delivery
            • Food quality or safety (which are the responsibility of partner restaurants)
            • Damages caused by unauthorized access to or use of your account
            Use of the app is at your own risk and provided on an "as-is" basis.

            10. Modifications to Terms
            We may update these Terms at any time. Continued use of the app after any changes constitutes your acceptance of the revised Terms.

            11. Termination
            We reserve the right to terminate or suspend your access without notice for any breach of these Terms or harmful conduct.

            12. Governing Law
            These Terms shall be governed by the laws of the Islamic Republic of Pakistan. Any disputes arising out of or in connection with these Terms shall be subject to the exclusive jurisdiction of the courts of Karachi, Pakistan.

            13. Contact Us
            Email: foodies554@gmail.com
            Address: Foodies App, Office #204, 2nd Floor, TechnoHub Plaza, Shahrah-e-Faisal, Karachi, Pakistan
        """.trimIndent()

        val privacyText = """
            Privacy Policy
            Effective Date: 12/05/2025
            App Name: Foodies ("we", "our", or "us")
            This Privacy Policy describes how we collect, use, disclose, and protect your personal information when you use our food ordering app.

            1. Information We Collect
            a. Personal Information
            • Name
            • Email address
            • Phone number
            • Delivery address
            • Payment information (processed securely via third-party providers)
            • Profile picture (optional)

            b. Usage Information
            • Order history
            • App usage patterns
            • Device information (e.g., OS, model, IP address)
            • Location data (only with your explicit permission)

            c. Cookies and Tracking Technologies
            We use cookies and similar technologies to:
            • Personalize your app experience
            • Analyze app performance and usage
            • Improve services and support

            2. How We Use Your Information
            • Process and deliver your food orders
            • Provide customer support and order tracking
            • Send app updates and promotional offers (only with your consent)
            • Improve the app’s functionality and user experience
            • Ensure user safety, prevent fraud, and comply with legal obligations

            3. Sharing Your Information
            We may share your information with:
            • Delivery partners, to fulfill and deliver your orders
            • Restaurants, to prepare your selected meals
            • Payment service providers, for secure transaction processing
            • Service providers (e.g., for hosting, analytics, or marketing)
            • Law enforcement or regulatory authorities, if required by applicable laws
            We do not sell your personal data to any third party.

            4. Data Security
            We apply reasonable administrative, technical, and physical safeguards to protect your information. However, no method of electronic transmission or storage is 100% secure, and we cannot guarantee absolute protection.

            5. Your Rights and Choices
            Depending on your jurisdiction, you may have the right to:
            • Access or update your personal data
            • Request deletion of your account and data
            • Opt out of receiving promotional emails or notifications
            • Withdraw consent for data processing
            To exercise these rights, contact us at: foodies554@gmail.com

            6. Children’s Privacy
            Foodies is not intended for children under the age of 13. We do not knowingly collect personal information from minors. If you believe a child has submitted personal information, please contact us so we can delete it.

            7. Third-Party Links
            Our app may contain links to third-party websites or services. We are not responsible for the privacy practices or content of those external platforms.

            8. Changes to This Policy
            We may update this Privacy Policy from time to time. Significant changes will be communicated via the app or email. Continued use of the app after changes constitutes your acceptance of the updated policy.

            9. Contact Us
            Email: foodies554@gmail.com
            Address: Foodies App, Office #204, 2nd Floor, TechnoHub Plaza, Shahrah-e-Faisal, Karachi, Pakistan
        """.trimIndent()

        policyText.movementMethod = ScrollingMovementMethod.getInstance()

        policyText.text = when (type) {
            "terms" -> termsText
            "privacy" -> privacyText
            else -> "No content to display."
        }

        closeButton.setOnClickListener {
            finish()
        }
    }
}
