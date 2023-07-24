package com.example.multicoverinsurance

import android.R.attr.author
import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.multicoverinsurance.authentication.Login
import com.example.multicoverinsurance.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if (user != null && user.email?.substringAfterLast("@") == "insurance.com") {
            val intent = Intent(this, AdminHomescreen::class.java)
            startActivity(intent)
            finish()
        }
        else
                if(user != null) {
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
            finish()
        }
        binding.directtologin.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.googleLogin.setOnClickListener{
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.WebClientId))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this,gso)
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
            }
        }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
           if(result.resultCode == Activity.RESULT_OK)
           {
               val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
               handleResults(task)
           }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
         if(task.isSuccessful){
             val account : GoogleSignInAccount? = task.result
             if(account!=null){
                 val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                 mAuth.signInWithCredential(credential).addOnCompleteListener{
                     if(task.isSuccessful){
                         Toast.makeText(baseContext, "Authentication Successful.",
                             Toast.LENGTH_SHORT).show()
                         val intent = Intent(this, Homescreen::class.java)
                         startActivity(intent)
                         finish()
                     }
                     else
                         Toast.makeText(baseContext, task.exception.toString(),
                             Toast.LENGTH_SHORT).show()
                 }

             }
             else
                 Toast.makeText(baseContext, "Authentication failed.",
                     Toast.LENGTH_SHORT).show()
         }
    }
}