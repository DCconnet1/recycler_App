package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dcconnet.sum.databinding.ActivityWorkerProfileBinding

class WorkerProfile : AppCompatActivity() {
    private lateinit var binding: ActivityWorkerProfileBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_profile)
        binding = ActivityWorkerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.showMap.setOnClickListener{
            startMapActivity()
            finish()


        }
    }

    private fun startMapActivity() {
        val intent = Intent(this, Mapdeneme::class.java)
        startActivity(intent)
    }




}