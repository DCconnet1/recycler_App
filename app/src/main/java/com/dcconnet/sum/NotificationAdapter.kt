package com.dcconnet.sum

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class NotificationAdapter(private val bildirimListesi : ArrayList<String>,private val context: Context) : RecyclerView.Adapter<NotificationAdapter.BildirimListesiVH>(){
    private val okunduListesi = ArrayList<Boolean>()
    init {

        okunduListesi.addAll(Collections.nCopies(bildirimListesi.size, false))
    }

    init {
        for (i in 0 until bildirimListesi.size) {
            okunduListesi.add(false)
        }
    }
    class BildirimListesiVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewdeneme1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BildirimListesiVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_row,parent,false)
        return BildirimListesiVH(itemView)
    }

    override fun onBindViewHolder(holder: BildirimListesiVH, position: Int) {
        val data = bildirimListesi[position]
        val isRead = okunduListesi[position]
        holder.itemView.findViewById<TextView>(R.id.textViewdeneme1).text = data
        if (isRead) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRead))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorUnread))
        }

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent =  Intent(holder.itemView.context,NotificationDetails::class.java)
            intent.putExtra("position", position)
            intent.putExtra("okunduListesi", okunduListesi)
            intent.putExtra("notificationTitle", data)
            intent.putExtra("notificationContent", "Bu bildirimin içeriği burada görünebilir.")
            okunduListesi[position] = true
            notifyDataSetChanged()
            holder.itemView.setBackgroundResource(R.color.colorRead)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return bildirimListesi.size
    }
}