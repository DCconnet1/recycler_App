package com.dcconnet.sum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class NotificationDetails : AppCompatActivity() {
    private lateinit var closeButton: Button
    private lateinit var okunduListesi: ArrayList<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_details)

        val notificationTitle = intent.getStringExtra("notificationTitle")
        val notificationContent = intent.getStringExtra("notificationContent")


        val notificationTitleTextView = findViewById<TextView>(R.id.notificationTitleTextView)
        val notificationContentTextView = findViewById<TextView>(R.id.notificationContentTextView)
        closeButton = findViewById(R.id.closeButton)

        notificationTitleTextView.text = notificationTitle
        notificationContentTextView.text = notificationContent

        closeButton.setOnClickListener {
            finish()
        }

    }
    override fun onPause() {
        super.onPause()
        okunduListesi = intent.getSerializableExtra("okunduListesi") as? ArrayList<Boolean> ?: ArrayList()
        val position = intent.getIntExtra("position", -1)
        val notificationReadStatus = okunduListesi[position]

        val sharedPreferences = getSharedPreferences("MessageReadStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("Notification$position", notificationReadStatus)
        editor.apply()
    }

}