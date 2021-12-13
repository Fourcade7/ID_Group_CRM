package com.example.id_group_crm.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.id_group_crm.Adapters.ChatAdapter
import com.example.id_group_crm.Adapters.UserAdapter
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Model.UserChat
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*
import kotlin.collections.ArrayList


class Chat() : Fragment() {

    lateinit var databaseReference:DatabaseReference
     var chatarraylist=ArrayList<UserChat>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_chat, container, false)




        databaseReference=FirebaseDatabase.getInstance().getReference().child("Chats")

        view.chatimageview.setOnClickListener {
            val calendar=Calendar.getInstance()
            var hour=calendar.get(Calendar.HOUR_OF_DAY)
            var minute=calendar.get(Calendar.MINUTE)
            if (view.chatedittext.text.toString().isEmpty()){
                view.chatedittext.setError("Напиши сообщение")
            }else{
                var uploadkey:String=databaseReference.push().key.toString()
                var userChat=UserChat(Constants.username,view.chatedittext.text.toString(),"$hour:$minute",uploadkey)
                databaseReference.child(uploadkey).setValue(userChat)
                view.chatedittext.setText("")

            }
        }

        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                chatarraylist.clear()
                for (datasnapshot in snapshot.children){
                    val userChat=datasnapshot.getValue(UserChat::class.java)
                    chatarraylist.add(userChat!!)
                }

                var chatadapter= context?.let { ChatAdapter(it,chatarraylist) }
                val layoutManager= LinearLayoutManager(context)
               // layoutManager.reverseLayout=true
                layoutManager.stackFromEnd=true
                view.chatrecyclerview.layoutManager=layoutManager
                view.chatrecyclerview.adapter=chatadapter
                //Toast.makeText(activity,chatarraylist.size,Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


         return view
    }


}