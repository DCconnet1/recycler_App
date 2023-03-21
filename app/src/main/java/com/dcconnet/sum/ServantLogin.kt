package com.dcconnet.sum

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.dcconnet.sum.databinding.ActivityMainBinding
import com.dcconnet.sum.databinding.ActivityServantLoginBinding

class ServantLogin : AppCompatActivity() {
    private lateinit var binding: ActivityServantLoginBinding
    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServantLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonListeners()



    }
    private fun setButtonListeners() {
        binding.servantSave.setOnClickListener {
            startServantRegister()
        }

        binding.servantLoginButton.setOnClickListener {
            val mUsername = binding.servantMail.text.toString()
            val mPassword = binding.servantPassword.text.toString()
            val user = User(mUsername, mPassword)
            loginUser(user)

        }


    }

    private fun loginUser(user: User) {
        repository.loginUser(user) { isLogin, isError ->
            if (!isError){
                if (isLogin) {
                    Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                    startMapActivity()


                }else {
                    Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT)
                        .show()
                }
            }else {
                Toast.makeText(this, "Bir hata meydana geldi", Toast.LENGTH_SHORT).show()
            }
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
                startMapActivity()


            }else{
                Toast.makeText(this, "Bir Hata Meydana Geldi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startServantRegister() {
        val intent = Intent(this, ServantRegister::class.java)
        startActivity(intent)

    }
    private fun startMapActivity() {
        val intent = Intent(this, Mapdeneme::class.java)
        startActivity(intent)

    }
}



