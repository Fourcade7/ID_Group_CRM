package com.example.id_group_crm.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.id_group_crm.Adapters.TaskAdapter
import com.example.id_group_crm.Adapters.UserAdapter
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Model.TaskModel
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_tasks.view.*
import androidx.recyclerview.widget.RecyclerView





class Tasks : Fragment() {
    val arrayList=ArrayList<TaskModel>()
    val chatarraylistarrayList2=ArrayList<TaskModel>()
    var taskadapter:TaskAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_tasks, container, false)


        view.edittexttasksearch.addTextChangedListener(object :TextWatcher{


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

        })







        val databaseReference= FirebaseDatabase.getInstance().getReference().child("Tasks")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                chatarraylistarrayList2.clear()
                var i=0
                for (datasnapshot in snapshot.children){
                    val tasks=datasnapshot.getValue(TaskModel::class.java)
                    arrayList.add(tasks!!)
                    if (arrayList.get(i).username==Constants.username){
                        chatarraylistarrayList2.add(tasks)
                    }
                    i++

                }

                if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){
                    taskadapter= context?.let { TaskAdapter(it,arrayList)}
                    val layoutManager= LinearLayoutManager(context)
//                layoutManager.reverseLayout=true
                    layoutManager.stackFromEnd=true
                    view.recyclerview3.layoutManager=layoutManager
                    view.recyclerview3.adapter=taskadapter

                }else{
                    taskadapter= context?.let { TaskAdapter(it,chatarraylistarrayList2)}
                    val layoutManager= LinearLayoutManager(context)
//                layoutManager.reverseLayout=true
                    layoutManager.stackFromEnd=true
                    view.recyclerview3.layoutManager=layoutManager
                    view.recyclerview3.adapter=taskadapter
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

//        view.floatingactionbutton.setOnClickListener {
//            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.linearlay1,Home())?.commit()
//        }
        return view
    }
    fun filter(text:String){
        val searcharraylist=ArrayList<TaskModel>()
        if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){
            for (item:TaskModel in arrayList){
                if (item.username.toLowerCase().contains(text.toLowerCase()) || item.taskmessage.toLowerCase().contains(text.toLowerCase()) || item.taskstarttime.toLowerCase().contains(text.toLowerCase()) || item.taskendtime.toLowerCase().contains(text.toLowerCase()) || item.taskstatus.toString().toLowerCase().contains(text.toLowerCase())){
                    searcharraylist.add(item)
                }
            }
            taskadapter?.let {
                it.filterList(searcharraylist)
            }
        }else{
            for (item:TaskModel in chatarraylistarrayList2){
                if (item.username.toLowerCase().contains(text.toLowerCase()) || item.taskmessage.toLowerCase().contains(text.toLowerCase()) || item.taskstarttime.toLowerCase().contains(text.toLowerCase()) || item.taskendtime.toLowerCase().contains(text.toLowerCase()) || item.taskstatus.toString().toLowerCase().contains(text.toLowerCase())){
                    searcharraylist.add(item)
                }
            }
            taskadapter?.let {
                it.filterList(searcharraylist)
            }
        }


    }


}