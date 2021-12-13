package com.example.id_group_crm.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Fragments.Home
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.recyclerview_item1.view.*

data class UserAdapter2(
    val context: Context,
    var userarrayList: ArrayList<Users>
): RecyclerView.Adapter<UserAdapter2.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.recyclerview_item1,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.textview1.text= userarrayList.get(position).username
        holder.itemView.textview2.text=userarrayList.get(position).userdatetime
        holder.itemView.relativelay1.setOnClickListener {
            val alertDialog= AlertDialog.Builder(context)
            alertDialog.setTitle("${userarrayList.get(position).username}")
            alertDialog.setMessage("${userarrayList.get(position).useraccount}\n${userarrayList.get(position).userpassword}\n${userarrayList.get(position).userdatetime}")
            alertDialog.setPositiveButton("да"){_,_->
               

            }

            alertDialog.setNeutralButton("Отмена"){_,_->

            }
            alertDialog.create()
            if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){

                alertDialog.show()
            }else{//
                Toast.makeText(context, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
            }

        }
        holder.itemView.relativelay1.setOnLongClickListener {
            val databaseReference=FirebaseDatabase.getInstance().getReference().child("Workers")
            val databaseReference2=FirebaseDatabase.getInstance().getReference().child("Users").child(userarrayList.get(position).userauthuid)
            val alertDialog=AlertDialog.Builder(context)
            alertDialog.setTitle("Вы удалите этого рабочий ?")
            alertDialog.setMessage("${userarrayList.get(position).username}")
            alertDialog.setPositiveButton("да"){_,_->
                databaseReference.child(userarrayList.get(position).useruploadkey.toString()).removeValue()
                databaseReference2.removeValue()
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
        return userarrayList.size
    }
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}