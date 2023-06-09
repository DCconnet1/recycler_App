package com.dcconnet.sum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dcconnet.sum.databinding.ActivityWorkerRegisterBinding
import java.util.UUID


class WorkerRegister : AppCompatActivity() {
    private lateinit var binding: ActivityWorkerRegisterBinding

    private val repository: AuthenticationRepository by lazy { AuthenticationRepository() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.workerSave.setOnClickListener {
            val sFirstName = binding.workerFirstName.text.toString()
            val sLastName = binding.workerLastName.text.toString()
            val sPassword = binding.workerPassword.text.toString()
            val sMail = binding.workerMail.text.toString()
            val sNumber = binding.workerNumber.text.toString()
            val sAge = binding.workerAge.text.toString().toInt()
            val sAdres = binding.workerAdress.text.toString()
            val sUserName = binding.workerUsername.text.toString()
            val sTruckNo = binding.workerTruckNo.text.toString()
            val uuid = UUID.randomUUID().toString()
            val user = User(truckNo = sTruckNo, firstname = sFirstName, password = sPassword, email = sMail, number = sNumber, userUid = uuid, lastname = sLastName, age = sAge, address = sAdres, username = sUserName)
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
                startWorkerLogin()

            }else{
                Toast.makeText(this, "Bir Hata Meydana Geldi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startWorkerLogin() {
        val intent = Intent(this, WorkerLogin::class.java)
        finish()
        startActivity(intent)
    }
}