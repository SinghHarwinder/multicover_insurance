package com.example.multicoverinsurance.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.multicoverinsurance.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.forgotButton.setOnClickListener{
            val email = binding.forgotEmail.text.toString()
            if(email.isNotEmpty()){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // email sent success
                        Toast.makeText(baseContext, "Password Reset email sent.",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, task.exception?.message.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}