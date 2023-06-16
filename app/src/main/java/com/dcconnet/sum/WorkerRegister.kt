package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityWorkerRegisterBinding


class WorkerRegister : AppCompatActivity() {
    private lateinit var binding: ActivityWorkerRegisterBinding

    private val repository: AuthenticationRepository1 by lazy { AuthenticationRepository1() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.workerSave.setOnClickListener {
            val wFirstname = binding.workerFirstName.text.toString()
            val wLastname = binding.workerLastName.text.toString()
            val wPassword = binding.workerPassword.text.toString()
            val wMail = binding.workerMail.text.toString()
            val wNumber = binding.workerNumber.text.toString()
            val user = User(firstname = wFirstname, lastname = wLastname, password =  wPassword, email = wMail,number =wNumber)
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
                startWorkerLogin()

            }else{
                Toast.makeText(this, "Bir Hata Meydana Geldi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startWorkerLogin() {
        val intent = Intent(this, WorkerLogin::class.java)
        startActivity(intent)
    }
}