package com.example.multicoverinsurance

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.multicoverinsurance.databinding.ActivityClaimsBinding
import com.example.multicoverinsurance.databinding.ClaimDaliogViewBinding
import com.example.multicoverinsurance.model.Insurance
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDate

class ClaimsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityClaimsBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private var uid : String? = ""
    private lateinit var imageUri: Uri
    private lateinit var storageReference: StorageReference
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.uid
        val intent = intent
        storageReference = FirebaseStorage.getInstance().reference
        val name = intent.getSerializableExtra("policyName") as Insurance
        if(name.name != null) {
            Log.d("policy", name.toString())
            binding.policyDetailName.text = name.name
            binding.policyDetailDesc.text = name.description
            //for when you have a image
               Glide.with(this)
                   .load(name.image)
                   .into(binding.policyDetailImage)

        }



        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child(uid.toString())
            .child("Active")
            .child(name.name)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Update the editable fields with the retrieved data
                    binding.expireDate.setText(dataSnapshot.child("expiry").getValue(String::class.java).toString())
                    binding.purchaseDate.setText(dataSnapshot.child("purchased").getValue(String::class.java).toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur during data retrieval
                Log.e("UserProfileActivity", "Error retrieving user profile: ${databaseError.message}")
            }
        })

        mDatabase = FirebaseDatabase.getInstance().reference
        binding.policyClaimButton.setOnClickListener{
                    val view = LayoutInflater.from(this).inflate(R.layout.claim_daliog_view,null)
                    val binding = ClaimDaliogViewBinding.bind(view)
                    binding.uploadProofText.setOnClickListener{
                        chooseImage()
                    }
            binding.dailogEdittextEmail.setText(mAuth.currentUser?.email)
            binding.dailogEdittextUsername.setText(mAuth.currentUser?.displayName)
            val dialog =   MaterialAlertDialogBuilder(this)
                .setTitle("Filing Claim")
                .setView(view)
                .setNegativeButton(resources.getString(R.string.decline)) { _, _ ->
                }
                .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                }
                .show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
                val text =   binding.dailogEdittextReason.text.toString()
                if(text.isNotEmpty())
                {
                    mDatabase.child(uid!!).child("Claim").child(name.name).setValue(name)
                    mDatabase.child(uid!!).child("Claim").child(name.name).child("reason").setValue(text)
                    mDatabase.child(uid!!).child("Claim").child(name.name).child("status").setValue("Under Review")
                    mDatabase.child(uid!!).child("Claim").child(name.name).child("date").setValue(LocalDate.now().toString())
                    uploadImageToFirebase(name.name)
                    Toast.makeText(baseContext,"Claim Submitted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(baseContext,"Enter Reason First", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.policyClaimCancelButton.setOnClickListener{
            if (uid != null) {
                mDatabase.child(uid!!).child("Active").child(name.name).removeValue()
                Toast.makeText(baseContext,"Policy cancelled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    // Now, you can upload the image to Firebase Storage

                }
            }
        }

    private fun uploadImageToFirebase(name: String) {
        // Get the current user ID (you may want to use other unique identifiers based on your app's logic)
        val userId = mAuth.currentUser?.uid ?: return

        // Create a reference to the image location in Firebase Storage
        val imageRef = storageReference.child("$userId/claims/$name.jpg")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Image upload successful
                // You can handle the success here, e.g., show a success message
               /* Glide.with(this)
                    .load(imageUri)
                    .into(binding.profilePicture)*/
                Toast.makeText(this, "Photo updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Toast.makeText(this, "Error updating photo", Toast.LENGTH_SHORT).show()
            }
    }
}