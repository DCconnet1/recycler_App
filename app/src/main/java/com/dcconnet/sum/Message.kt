package com.dcconnet.sum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Message : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        recyclerView = findViewById(R.id.recyclerView)
        val mesajListesi = ArrayList<String>()
        mesajListesi.add("Mesaj 1")
        mesajListesi.add("Mesaj 2")
        mesajListesi.add("Mesaj 3")
        mesajListesi.add("Mesaj 4")
        mesajListesi.add("Mesaj 5")





        recyclerView.layoutManager = LinearLayoutManager(this)



        val adapter = MessageAdapter(mesajListesi,applicationContext)
        recyclerView.adapter = adapter

    }
}