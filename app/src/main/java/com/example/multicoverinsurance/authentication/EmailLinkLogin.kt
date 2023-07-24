package com.example.multicoverinsurance.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.multicoverinsurance.R
import com.example.multicoverinsurance.databinding.ActivityEmailLinkLoginBinding
import com.example.multicoverinsurance.databinding.ActivityLoginBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class EmailLinkLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEmailLinkLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_link_login)
        binding = ActivityEmailLinkLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.sendLink.setOnClickListener {
            val email1 = binding.email.text.toString().trim()

            val actionCodeSettings = ActionCodeSettings.newBuilder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                .setUrl("multicover-insurance-57e05.firebaseapp.com")
                // This must be true
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                    "com.example.multicoverinsurance",
                    true, /* installIfNotAvailable */
                    "12"    /* minimumVersion */
                )
                .build()

            var addOnCompleteListener = auth.sendSignInLinkToEmail(email1, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }
        }
    }


    private fun handleEmailLinkSignIn() {
        val intent: Intent = intent
        val emailLink: Uri? = intent.data

        if (emailLink != null) {
            val email = intent.getStringExtra("email")

            auth.signInWithEmailLink(email!!, emailLink.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmailLink:success")
                        val user = auth.currentUser
                        // Handle successful sign-in
                    } else {
                        Log.w(TAG, "signInWithEmailLink:failure", task.exception)
                        // Handle sign-in failure
                    }
                }
        }
    }
}