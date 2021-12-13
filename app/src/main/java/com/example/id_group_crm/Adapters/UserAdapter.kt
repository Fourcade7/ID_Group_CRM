package com.example.id_group_crm.Adapters

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
import kotlinx.android.synthetic.main.recyclerview_item1.view.*

data class UserAdapter(
    val context: Context,
    var userarrayList: ArrayList<Users>
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.recyclerview_item1,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.textview1.text= userarrayList.get(position).username
        holder.itemView.textview2.text=userarrayList.get(position).userdatetime
        holder.itemView.relativelay1.setOnClickListener {
            val intent=Intent(context,TaskActivity::class.java)
            intent.apply {
                putExtra("key",userarrayList.get(position).username)
//                context.applicationContext.supportFragmentManager.beginTransaction()?.replace(R.id.linearlay1, Home())?.commit()
                

            }
            if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){

                context.startActivity(intent)
            }else{//
                Toast.makeText(context, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun getItemCount(): Int {
        return userarrayList.size
    }
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}