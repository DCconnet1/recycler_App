package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityWorkerLoginBinding

class WorkerLogin : AppCompatActivity() {
    private lateinit var binding: ActivityWorkerLoginBinding

    private val repository: AuthenticationRepository1 by lazy { AuthenticationRepository1() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWorkerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonListeners()
        val isUserLoggedIn: Boolean =
            SharedPrefUtil.getInstance(this).getString(SharedPrefUtil.USER_EMAIL, "") != ""
        if (isUserLoggedIn) {
            startWorkerProfile()
        }
    }
    private fun setButtonListeners() {
        binding.workerSave.setOnClickListener{
            startWorkerRegister()

        }

        binding.workerLoginButton.setOnClickListener {
            val wMail = binding.workerEmail.text.toString()
            val wPassword = binding.workerPassword.text.toString()
            val user = User(wMail, wPassword)
            loginUser(user)
        }
    }

    private fun loginUser(user: User) {
        repository.loginUser(user) { isLogin, isError ->
            if (!isError) {
                if (isLogin) {
                    Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                    SharedPrefUtil.getInstance(this)
                        .saveString(SharedPrefUtil.USER_EMAIL, user.email.orEmpty())
                    startWorkerProfile()
                } else {
                    Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show()
                }
            } else {
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
                startWorkerLogin()

            }else{
                Toast.makeText(this, "Bir Hata Meydana Geldi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startWorkerProfile() {
        val intent = Intent(this, WorkerProfile::class.java)
        finish()
        startActivity(intent)
    }
    private fun startWorkerRegister() {
        val intent = Intent(this, WorkerRegister::class.java)
        startActivity(intent)
    }
    private fun startWorkerLogin() {
        val intent = Intent(this, WorkerLogin::class.java)
        startActivity(intent)
    }


}
