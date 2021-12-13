package com.example.id_group_crm.Activitys

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.id_group_crm.Adapters.UserAdapter
import com.example.id_group_crm.Adapters.UserAdapter2
import com.example.id_group_crm.Constants
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class UserActivity : AppCompatActivity() {

    val arrayList=ArrayList<Users>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val databaseReference= FirebaseDatabase.getInstance().getReference().child("Workers")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (datasnapshot in snapshot.children){
                    val users=datasnapshot.getValue(Users::class.java)
                    arrayList.add(users!!)
                }

                val userAdapter2=UserAdapter2(this@UserActivity,arrayList)
                val layoutManager= LinearLayoutManager(this@UserActivity)
//                layoutManager.reverseLayout=true
//                layoutManager.stackFromEnd=true
                recyclerview4.layoutManager=layoutManager
                recyclerview4.adapter=userAdapter2
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        imageviewadduser2.setOnClickListener {
            if(Constants.useruid.equals("s16wxyhmGKRRr9AXV2Z8w14oSVc2")){
                startActivity(Intent(this@UserActivity,Create_User::class.java))
            }else{//
                Toast.makeText(this@UserActivity, "Это невозможно для тебя", Toast.LENGTH_SHORT).show()
            }


        }
    }
}