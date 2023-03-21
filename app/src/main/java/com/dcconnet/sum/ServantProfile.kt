package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dcconnet.sum.databinding.ActivityServantProfileBinding
import com.dcconnet.sum.databinding.ActivityWorkerProfileBinding

class ServantProfile : AppCompatActivity() {
    private lateinit var binding: ActivityServantProfileBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servant_profile)
        binding = ActivityServantProfileBinding.inflate(layoutInflater)
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