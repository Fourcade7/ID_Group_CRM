package com.example.id_group_crm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import kotlinx.android.synthetic.main.recyclerview_item1.view.*

data class UserAdapter(
    val context: Context,
    val userarrayList: ArrayList<Users>
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.recyclerview_item1,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.textview1.text= userarrayList.get(position).username
        holder.itemView.textview2.text=userarrayList.get(position).userdatetime
        holder.itemView.relativelay1.setOnClickListener {  }
    }

    override fun getItemCount(): Int {
        return userarrayList.size
    }
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}