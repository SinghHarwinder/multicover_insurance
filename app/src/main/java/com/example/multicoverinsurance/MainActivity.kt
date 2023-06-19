package com.example.multicoverinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.multicoverinsurance.authentication.Login
import com.example.multicoverinsurance.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if (user != null) {
            binding.dashboardUsername.text = "Hi, " + user.displayName
        }
    }

    fun logOut(view: View) {
        FirebaseAuth.getInstance().signOut();
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}