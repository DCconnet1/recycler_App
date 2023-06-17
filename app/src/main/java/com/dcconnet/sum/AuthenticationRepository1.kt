package com.dcconnet.sum

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AuthenticationRepository1 {

    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("users")


    fun isUserAlreadyRegistered(user: User, isUserExist: (Boolean, Boolean) -> Unit) {
        user.firstname?.let { databaseBook?.child(it) }?.get()?.addOnSuccessListener {
            if (!it.exists()) {
                isUserExist(false, false)
            } else {
                isUserExist(true, false)
            }
        }?.addOnFailureListener {
            isUserExist(false, true)
        }
    }


    fun createUser(user: User, onSuccessListener: (Boolean) -> Unit) {
        user.firstname?.let { wMail -> databaseBook?.child(wMail.lowercase()) }
            ?.setValue(user)?.addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccessListener(true)
            } else {
                onSuccessListener(false)
            }
        }
    }


    fun loginUser(user: User, isLoginSuccessful: (Boolean, Boolean) -> Unit) {
        user.email?.let { wEmail ->
            databaseBook?.child(wEmail.lowercase())?.get()?.addOnSuccessListener {
                if (it.hasChildren()) {
                    val firstname = it.getValue(User::class.java)?.firstname?.lowercase()
                    val lastname = it.getValue(User::class.java)?.lastname?.lowercase()
                    val password = it.getValue(User::class.java)?.password?.lowercase()
                    val email = it.getValue(User::class.java)?.email?.lowercase()
                    val number = it.getValue(User::class.java)?.number?.lowercase()

                    if (password.equals(user.password) && email.equals(user.email)) {
                        isLoginSuccessful(true, false)
                    } else {
                        isLoginSuccessful(false, false)
                    }
                } else {
                    isLoginSuccessful(false, false)
                }
            }?.addOnFailureListener {
                isLoginSuccessful(false, true)
            }
        }
    }
}



