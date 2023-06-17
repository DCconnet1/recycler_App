package com.dcconnet.sum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dcconnet.sum.databinding.ActivityKullaniciBilgileriBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*

class KullaniciBilgileri : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("users")

    private lateinit var binding: ActivityKullaniciBilgileriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKullaniciBilgileriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val localMail = SharedPrefUtil.getInstance(this).getString(SharedPrefUtil.USER_EMAIL, "")

        val dataListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children
                        .map { it.getValue(User::class.java) }
                        .firstOrNull {
                            it?.email == localMail
                        }?.let { user ->
                            setUi(user)
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

    private fun setUi(user: User) {
        binding.userNameSurname.text = user.firstname + " " + user.lastname
        binding.paperWeight.text = user.paperWeight
        binding.plasticWeight.text = user.plasticWeight
        binding.glassWeight.text = user.glassWeight
        binding.totalWeight.text = user.totalWaste
        binding.workerMailInput.text = user.email
        binding.workerNumberInput.text = user.number
        binding.workerLocation.text = user.address
        binding.workerTruckNo.text = user.truckNo
    }
}

