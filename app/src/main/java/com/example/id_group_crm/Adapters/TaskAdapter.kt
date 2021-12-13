package com.example.id_group_crm.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.id_group_crm.Activitys.UserActivity
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Model.TaskModel
import com.example.id_group_crm.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.recyclerview_item1.view.*
import kotlinx.android.synthetic.main.recyclerview_item3.view.*

class TaskAdapter(
    val context: Context,
    var tasksarrayList: ArrayList<TaskModel>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    val databaseReference= FirebaseDatabase.getInstance().getReference().child("Tasks")
    var click=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.recyclerview_item3,parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.itemView.textviewtaskusername.text= tasksarrayList.get(position).username
        holder.itemView.textviewtasktime.setText("Задача От ${tasksarrayList.get(position).taskstarttime} до ${tasksarrayList.get(position).taskendtime}")
        holder.itemView.textviewtaskmessage.text=tasksarrayList.get(position).taskmessage

        when(tasksarrayList.get(position).taskstatus){
            "s0"->{holder.itemView.imageviewstatus.setImageResource(R.drawable.error)
                holder.itemView.textviewstatus.setText("s0")
            }
            "s1"-> {
                holder.itemView.imageviewstatus.setImageResource(R.drawable.loading)
                holder.itemView.textviewstatus.setText("s1")
            }
            "s2"-> {
                holder.itemView.imageviewstatus.setImageResource(R.drawable.done)
                holder.itemView.textviewstatus.setText("s2")
            }
        }
        holder.itemView.relativelaytask.setOnClickListener {
            if (click){
                holder.itemView.linearlaytask.visibility=View.GONE
                click=false
            }else{
                holder.itemView.linearlaytask.visibility=View.VISIBLE
                click=true
            }

        }


        holder.itemView.buttonunderstand.setOnClickListener {
            var uploadkey:String=tasksarrayList.get(position).useruploadkey.toString()
            val taskmodel=TaskModel(tasksarrayList.get(position).username,tasksarrayList.get(position).taskmessage,"s1",tasksarrayList.get(position).taskstarttime,tasksarrayList.get(position).taskendtime,uploadkey)
            databaseReference.child(uploadkey).setValue(taskmodel)
        }
        holder.itemView.buttondone.setOnClickListener {
            var uploadkey:String=tasksarrayList.get(position).useruploadkey.toString()
            val taskmodel=TaskModel(tasksarrayList.get(position).username,tasksarrayList.get(position).taskmessage,"s2",tasksarrayList.get(position).taskstarttime,tasksarrayList.get(position).taskendtime,uploadkey)
            databaseReference.child(uploadkey).setValue(taskmodel)
        }

        holder.itemView.relativelaytask.setOnLongClickListener {
            val databaseReference= FirebaseDatabase.getInstance().getReference().child("Tasks")
            val alertDialog= AlertDialog.Builder(context)
            alertDialog.setTitle("Удалить задачу")
            alertDialog.setMessage("Вы хотите удалить задачу ?")
            alertDialog.setPositiveButton("да"){_,_->
                databaseReference.child(tasksarrayList.get(position).useruploadkey!!).removeValue()

            }
            alertDialog.setNegativeButton("Нет"){_,_->

            }
            alertDialog.setNeutralButton("Отмена"){_,_->

            }
            alertDialog.create()
            if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){

                alertDialog.show()
            }else{//
                Toast.makeText(context, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
            }


            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return tasksarrayList.size
    }

    fun filterList(filteredList: ArrayList<TaskModel>) {
        tasksarrayList = filteredList
        notifyDataSetChanged()
    }
    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}