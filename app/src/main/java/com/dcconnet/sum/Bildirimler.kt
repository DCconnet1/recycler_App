package com.dcconnet.sum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Bildirimler : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bildirimler)
        recyclerView = findViewById(R.id.recyclerViewNotification)
        val bildirimListesi = ArrayList<String>()
        bildirimListesi.add("Bildirim 1")
        bildirimListesi.add("Bildirim 2")
        bildirimListesi.add("Bildirim 3")
        bildirimListesi.add("Bildirim 4")
        bildirimListesi.add("Bildirim 5")





        recyclerView.layoutManager = LinearLayoutManager(this)



        val adapter = NotificationAdapter(bildirimListesi,applicationContext)
        recyclerView.adapter = adapter

    }
}