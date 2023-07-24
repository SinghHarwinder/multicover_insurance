package com.example.multicoverinsurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multicoverinsurance.adapters.AdminClaimAdapter
import com.example.multicoverinsurance.adapters.ClaimAdapter
import com.example.multicoverinsurance.databinding.ActivityAdminHomescreenBinding
import com.example.multicoverinsurance.databinding.ActivityHomescreenBinding
import com.example.multicoverinsurance.databinding.FragmentClaimsBinding
import com.example.multicoverinsurance.model.AdminClaims
import com.example.multicoverinsurance.model.Claims
import com.example.multicoverinsurance.model.Insurance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AdminHomescreen : AppCompatActivity() {
    private lateinit var mDatabase: DatabaseReference
    private lateinit var recyclerView : RecyclerView
    private lateinit var itemAdapter : AdminClaimAdapter
    private lateinit var binding: ActivityAdminHomescreenBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var policiesList : MutableList<AdminClaims>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        policiesList = ArrayList()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.keepSynced(true)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                policiesList.clear()

                for( users in dataSnapshot.children)
                {
                    val uid = users.key.toString()
                    val email =  users.child("info").child("email").value.toString()
                    for(status in users.children){
                       Log.d("status",status.key.toString())
                       if(status.key.toString() == "Claim")
                       {
                           for (policy in status.children)
                           {
                               val name = policy.child("name").value.toString()
                               val reason = policy.child("reason").value.toString()
                               val date = policy.child("date").value.toString()
                               val status = policy.child("status").value.toString()
                               val desc = policy.child("description").value.toString()
                               val image = policy.child("image").value.toString()
                               val data = AdminClaims(uid,email,desc,image,name, reason, date, status)
                               Log.d("Firebase", data.toString())
                               if (policy != null)
                               {
                                   policiesList.add(data)
                                   setAdapter(binding)
                                   itemAdapter.notifyDataSetChanged()
                               }
                           }
                       }

                   }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        mDatabase.addValueEventListener(postListener)

        binding.adminLogout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setAdapter(binding: ActivityAdminHomescreenBinding) {
        recyclerView = binding.adminRecyclerview
       itemAdapter = AdminClaimAdapter(policiesList, this, "Active")
        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}