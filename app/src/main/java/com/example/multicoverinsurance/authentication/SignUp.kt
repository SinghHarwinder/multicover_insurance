package com.example.multicoverinsurance.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.multicoverinsurance.MainActivity
import com.example.multicoverinsurance.R
import com.example.multicoverinsurance.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class SignUp : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.signup.setOnClickListener{
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val passwordA = binding.signupPasswordAgain.text.toString()
            val name = binding.signupName.text.toString()
            if(email.isEmpty() || password.isEmpty() || passwordA.isEmpty())
            {    Toast.makeText(baseContext,"Please don't leave fields empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password != passwordA){
                Toast.makeText(baseContext,"Passwords don't match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(name.isEmpty()){
                Toast.makeText(baseContext,"Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build()
                        user?.updateProfile(profileUpdates);
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}