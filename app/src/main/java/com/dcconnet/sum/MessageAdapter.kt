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

class MessageAdapter(private val mesajListesi : ArrayList<String>,private val context: Context) : RecyclerView.Adapter<MessageAdapter.MesajListesiVH>(){
    private val okunduListesi = ArrayList<Boolean>()
    init {

        okunduListesi.addAll(Collections.nCopies(mesajListesi.size, false))
    }

    init {
        for (i in 0 until mesajListesi.size) {
            okunduListesi.add(false)
        }
    }
    class MesajListesiVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewdeneme12)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesajListesiVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_row,parent,false)
        return MesajListesiVH(itemView)
    }

    override fun onBindViewHolder(holder: MesajListesiVH, position: Int) {
        val data = mesajListesi[position]
        val isRead = okunduListesi[position]
        holder.itemView.findViewById<TextView>(R.id.textViewdeneme12).text = data
        if (isRead) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRead))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorUnread))
        }

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent =  Intent(holder.itemView.context,MessageDetails::class.java)
            intent.putExtra("position", position)
            intent.putExtra("okunduListesi", okunduListesi)
            intent.putExtra("messageTitle", data)
            intent.putExtra("messageContent", "Bu mesajın içeriği burada görünebilir.")
            okunduListesi[position] = true
            notifyDataSetChanged()
            holder.itemView.setBackgroundResource(R.color.colorRead)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return mesajListesi.size
    }
}