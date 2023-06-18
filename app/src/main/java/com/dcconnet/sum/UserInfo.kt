package com.dcconnet.sum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dcconnet.sum.databinding.ActivityKullaniciBilgileriBinding
import com.dcconnet.sum.databinding.ActivityUserInfoBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*

class UserInfo : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("users")

    private lateinit var binding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
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
        binding.userNameSurname.text = user.username
        binding.userNameSurname1.text = user.firstname + " " + user.lastname
        binding.age.text = (2023 - user.age).toString()
        binding.totalWeight.text = user.totalWaste + " kg"
        binding.workerMailInput.text = user.email
        binding.workerNumberInput.text = user.number
        binding.workerLocation.text = user.address
        binding.workerTruckNo.text = user.truckNo






    }
}

