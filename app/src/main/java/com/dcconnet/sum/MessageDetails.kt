package com.dcconnet.sum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MessageDetails : AppCompatActivity() {
    private lateinit var closeButton: Button
    private lateinit var okunduListesi: ArrayList<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)

        val messageTitle = intent.getStringExtra("messageTitle")
        val messageContent = intent.getStringExtra("messageContent")


        val messageTitleTextView = findViewById<TextView>(R.id.messageTitleTextView)
        val messageContentTextView = findViewById<TextView>(R.id.messageContentTextView)
        closeButton = findViewById(R.id.closeButton)

        messageTitleTextView.text = messageTitle
        messageContentTextView.text = messageContent

        closeButton.setOnClickListener {
            finish()
        }

    }
    override fun onPause() {
        super.onPause()
        okunduListesi = intent.getSerializableExtra("okunduListesi") as? ArrayList<Boolean> ?: ArrayList()
        val position = intent.getIntExtra("position", -1)
        val messageReadStatus = okunduListesi[position]

        val sharedPreferences = getSharedPreferences("MessageReadStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("Message$position", messageReadStatus)
        editor.apply()
    }

}
