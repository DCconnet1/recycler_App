package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityServantProfileBinding
import com.dcconnet.sum.databinding.ActivityWorkerProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WorkerProfile : AppCompatActivity() {
    private lateinit var binding: ActivityWorkerProfileBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }
    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_profile)
        binding = ActivityWorkerProfileBinding.inflate(layoutInflater)
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


        binding.getWork.setOnClickListener{
            Toast.makeText(this,"Lütfen bağlantınızın güçlü olduğu yere geçin ve cihazın GPS özelliğini açınız.",
                Toast.LENGTH_SHORT).show()
            //  startTrashBinsActivity()

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





    }
    private fun setUi(user: User) {
        binding.textView2.text = user.email.toString()
        binding.textView.text = user.username.toString()
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

    /* private fun startTrashBinsActivity() {
         val intent = Intent(this, TrashBins::class.java)
         startActivity(intent)
     }*/

}