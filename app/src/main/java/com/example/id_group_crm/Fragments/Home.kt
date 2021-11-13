package com.example.id_group_crm.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.id_group_crm.Adapters.UserAdapter
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*
import kotlin.collections.ArrayList


class Home : Fragment() {

    val arrayList=ArrayList<Users>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view=inflater.inflate(R.layout.fragment_home, container, false)

        val databaseReference= FirebaseDatabase.getInstance().getReference().child("Workers")
        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (datasnapshot in snapshot.children){
                    val users=datasnapshot.getValue(Users::class.java)
                    arrayList.add(users!!)
                }

                val userAdapter=UserAdapter(activity!!.applicationContext,arrayList)
                val layoutManager=LinearLayoutManager(activity!!.applicationContext)
//                layoutManager.reverseLayout=true
//                layoutManager.stackFromEnd=true
               view.recyclerview1.layoutManager=layoutManager
                view.recyclerview1.adapter=userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return view
    }


}