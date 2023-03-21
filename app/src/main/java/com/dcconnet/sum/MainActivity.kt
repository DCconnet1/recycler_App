package com.dcconnet.sum


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonListener()



    }


    private fun setButtonListener() {

        binding.servant.setOnClickListener {
            val intent = Intent(this,ServantLogin::class.java)
                startActivity(intent)

        }
        binding.worker.setOnClickListener {
            val intent = Intent(this,WorkerLogin::class.java)
                 startActivity(intent)

        }
    }







}


