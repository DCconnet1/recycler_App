package com.dcconnet.sum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*

class KullaniciBilgileri : AppCompatActivity() {
    private lateinit var textViewLastName: TextView
    private lateinit var textViewName: TextView
    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici_bilgileri)

        textViewName = findViewById(R.id.textViewName)
        textViewLastName = findViewById(R.id.textViewLastName)


        val dataListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children
                        .map { it.getValue(User::class.java) }
                        .firstOrNull {
                            it?.email == UserSession.email
                        }?.let { user ->
                            textViewName.text = user.firstname
                        }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Veri çekme hatası: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        databaseBook?.addValueEventListener(dataListener1)


    }
}

