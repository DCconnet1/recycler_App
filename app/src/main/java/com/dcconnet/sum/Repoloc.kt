package com.dcconnet.sum

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.sql.Timestamp
import java.util.concurrent.TimeUnit


class Repoloc {

    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("locations")
    private val db = Firebase.firestore




    fun setCurrentLocation(loca: android.location.Location) {

       /* val loca = hashMapOf(
            "lat" to loca.latitude,
            "lon" to loca.longitude,
            "name" to System.currentTimeMillis()
        )
        db.collection("locations")
            .add(loca)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }*/










        FirebaseDatabase.getInstance().reference.child("locations")
            .child( " " + System.currentTimeMillis()).setValue(
            MyLocation(
                lat = loca.latitude,
                lon = loca.longitude
            )
        )
    }




    //Kullanıcı olusturma fonksiyonu, onSuccessListener(kullanıcı başarılı bir şekilde kayıt oldu mu)
    fun createUser(loca: Location, onSuccessListener: (Boolean) -> Unit) {
        loca.name?.let { rLat -> databaseBook?.child(rLat) }?.setValue(loca)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccessListener(true)
                } else {
                    onSuccessListener(false)
                }
            }
    }

    //Kullanıcı girişini yapar isLoginSuccessful()
    fun loginUser(loca: Location, isLoginSuccessful: (Boolean, Boolean) -> Unit) {
        loca.name?.let { rLat -> databaseBook?.child(rLat) }?.get()?.addOnSuccessListener {
            if (it.hasChildren()) {
                val lng = it.getValue(Location::class.java)?.longitude
                val lat = it.getValue(Location::class.java)?.latitude
                if (lat!!.equals(loca.latitude) && lng!!.equals(loca.longitude)) {
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



