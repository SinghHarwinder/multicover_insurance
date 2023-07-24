package com.example.multicoverinsurance
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class PersistenceStorage : AppCompatActivity() {
   fun onCreate() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}