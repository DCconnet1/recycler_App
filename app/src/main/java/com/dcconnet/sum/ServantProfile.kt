package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityServantProfileBinding

class ServantProfile : AppCompatActivity() {
    private lateinit var binding: ActivityServantProfileBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servant_profile)
        binding = ActivityServantProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getWork.setOnClickListener{
            startTrashBinsActivity()

        }
        binding.mesajlar.setOnClickListener{
            startMesajActivity()
        }
        binding.bildirimler.setOnClickListener{
            startBildirimActivity()
        }
        binding.kullanicibilgileri.setOnClickListener{
            startKullaniciBilgileriActivity()
        }

// map altında liste -- profil güncelleme -- kutu eklerken tür seç --



    }

    private fun startMesajActivity(){
        val intent = Intent(this,Message::class.java)
        startActivity(intent)
    }
    private fun startBildirimActivity(){
        val intent = Intent(this,Bildirimler::class.java)
        startActivity(intent)
    }
    private fun startKullaniciBilgileriActivity(){
        val intent = Intent(this,KullaniciBilgileri::class.java)
        startActivity(intent)
    }

   private fun startTrashBinsActivity() {
        val intent = Intent(this, TrashBins::class.java)
        startActivity(intent)
    }

}