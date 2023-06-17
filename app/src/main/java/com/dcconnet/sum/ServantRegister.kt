package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityServantRegisterBinding
import java.util.UUID

class ServantRegister : AppCompatActivity() {
    private lateinit var binding: ActivityServantRegisterBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servant_register)
        binding = ActivityServantRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.servantSave.setOnClickListener {
            val sFirstName = binding.servantFirstName.text.toString()
            val sLastName = binding.servantLastName.text.toString()
            val sPassword = binding.servantPassword.text.toString()
            val sMail = binding.servantMail.text.toString()
            val sNumber = binding.servantNumber.text.toString()
            val sAge = binding.servantAge.text.toString().toInt()
            val sAdres = binding.servantAdress.text.toString()
            val sUsername = binding.servantUserName.text.toString()
            val uuid = UUID.randomUUID().toString()
            val user = User(firstname = sFirstName, password = sPassword, email = sMail, number = sNumber, userUid = uuid, lastname = sLastName, age = sAge, address = sAdres, username = sUsername)
            checkIfUserExist(user)

        }
    }
    private fun checkIfUserExist(user: User) {
        repository.isUserAlreadyRegistered(user){ isUserExist, isError ->
            if (!isError){
                if (!isUserExist){
                    registerUser(user)
                }else{
                    Toast.makeText(this, "Kullanıcı zaten kayıtlı", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(user: User) {
        repository.createUser(user){
            if (it) {
                Toast.makeText(this, "Hesap Başırı Bir Şekilde Oluşturuldu", Toast.LENGTH_SHORT)
                    .show()
                SharedPrefUtil.getInstance(this).saveString(SharedPrefUtil.USER_EMAIL, user.email ?: "")
                startServantLogin()

            }else{
                Toast.makeText(this, "Bir Hata Meydana Geldi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startServantLogin() {
        val intent = Intent(this, ServantLogin::class.java)
        finish()
        startActivity(intent)
    }
}