package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityWorkerProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class WorkerProfile : AppCompatActivity() {
    private lateinit var binding: ActivityWorkerProfileBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_profile)
        binding = ActivityWorkerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //getWorkerProfileData()
        binding.showMap.setOnClickListener{
            startMapActivity()
            finish()


        }
    }

    /*private fun getWorkerProfileData() {
        val dataListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children
                        .map { it.getValue(User::class.java) }
                        .firstOrNull {
                            it?.email == UserSession.email
                        }?.let { user ->

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
    }*/

    private fun startMapActivity() {
        val intent = Intent(this, Mapdeneme::class.java)
        startActivity(intent)
    }




}