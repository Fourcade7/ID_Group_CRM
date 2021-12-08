package com.example.id_group_crm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.id_group_crm.Model.UserChat
import com.example.id_group_crm.R
import kotlinx.android.synthetic.main.recyclerview_item2.view.*

class ChatAdapter(
    val context: Context,
    var chatarraylist: ArrayList<UserChat>


):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.recyclerview_item2,parent,false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.itemView.chattextview1.setText(chatarraylist.get(position).username+"")
        holder.itemView.chattextview3.setText(chatarraylist.get(position).usermessage)
        holder.itemView.chattextview2.setText(chatarraylist.get(position).datetime)
    }

    override fun getItemCount(): Int {
      return chatarraylist.size
    }
}