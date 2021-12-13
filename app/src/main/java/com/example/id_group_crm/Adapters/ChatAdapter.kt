package com.example.id_group_crm.Adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Model.UserChat
import com.example.id_group_crm.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item2.view.*
import kotlinx.android.synthetic.main.recyclerview_item2.view.chatrelativelay1
import kotlinx.android.synthetic.main.recyclerview_item2.view.chattextview1
import kotlinx.android.synthetic.main.recyclerview_item2.view.chattextview2
import kotlinx.android.synthetic.main.recyclerview_item2.view.chattextview3
import kotlinx.android.synthetic.main.recyclerview_itemright2.view.*

class ChatAdapter(
    val context: Context,
    var chatarraylist: ArrayList<UserChat>


):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var MESSAGE_TYPE_LEFT: Int = 0
    var MESSAGE_TYPE_RIGHT: Int = 1

    class ChatViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view:View
        if (viewType==MESSAGE_TYPE_LEFT){
            view= LayoutInflater.from(context).inflate(R.layout.recyclerview_item2,parent,false)
        }else{
            view= LayoutInflater.from(context).inflate(R.layout.recyclerview_itemright2,parent,false)

        }
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.itemView.chattextview1.setText(chatarraylist.get(position).username)
        holder.itemView.chattextview3.setText(chatarraylist.get(position).usermessage)
        holder.itemView.chattextview2.setText(chatarraylist.get(position).datetime)


        holder.itemView.chatrelativelay1.setOnLongClickListener {
           var databaseReference= FirebaseDatabase.getInstance().getReference().child("Chats")
            val alertDialog=AlertDialog.Builder(context)
            alertDialog.setTitle("Удалить сообщение")
            alertDialog.setMessage("Вы хотите удалить это сообщение?")
            alertDialog.setPositiveButton("да"){_,_->
                databaseReference.child(chatarraylist.get(position).useruploadkey!!).removeValue()

            }
            alertDialog.setNegativeButton("Нет"){_,_->

            }
            alertDialog.setNeutralButton("Отмена"){_,_->

            }
            alertDialog.create()
            alertDialog.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
      return chatarraylist.size
    }

    override fun getItemViewType(position: Int): Int {

        if (chatarraylist.get(position).username==Constants.username){
            return MESSAGE_TYPE_RIGHT;
        }else {
            return MESSAGE_TYPE_LEFT;
        }
    }
}