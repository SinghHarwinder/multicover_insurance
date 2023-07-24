package com.example.multicoverinsurance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.multicoverinsurance.databinding.ActivityClaimDetailsBinding
import com.example.multicoverinsurance.databinding.ActivityPolicyDetailsBinding
import com.example.multicoverinsurance.model.AdminClaims
import com.example.multicoverinsurance.model.Insurance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDate

class ClaimDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityClaimDetailsBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val name = intent.getSerializableExtra("claimDetail") as AdminClaims
        binding.claimDetailEmail.text = name.email
        binding.policyDetailName.text = name.name
        binding.policyDetailDesc.text = name.desc
        Glide.with(this)
            .load(name.image)
            .into(binding.policyDetailImage)
        val insurance = name.name
        val userId = name.uid
        // Create a reference to the image location in Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("$userId/claims/$insurance.jpg")
        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            // Load and display the image using Glide
            Glide.with(this)
                .load(downloadUrl)
                .into(binding.claimDetailImage)
        }.addOnFailureListener {
            // Handle the error
        }
        mDatabase = FirebaseDatabase.getInstance().reference
        binding.claimDetailAccept.setOnClickListener{
            mDatabase.child(name.uid).child("Claim").child(name.name).child("status").setValue("Accepted")
            Toast.makeText(baseContext,"Claim Accepted", Toast.LENGTH_SHORT).show()
        }
        binding.claimDetailDecline.setOnClickListener{
            mDatabase.child(name.uid).child("Claim").child(name.name).child("status").setValue("Declined")
            Toast.makeText(baseContext,"Claim Declined", Toast.LENGTH_SHORT).show()
        }
    }
}