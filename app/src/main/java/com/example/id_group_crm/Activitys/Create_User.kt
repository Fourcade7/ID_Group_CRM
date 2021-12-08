package com.example.id_group_crm.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.id_group_crm.Model.UserMap
import com.example.id_group_crm.Model.Users
import com.example.id_group_crm.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class Create_User : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        val firebaseAuth=FirebaseAuth.getInstance()

        buttonregisteruser.setOnClickListener {
           if (checkedittextempty()){
               val calendar=Calendar.getInstance()
               var day=calendar.get(Calendar.DAY_OF_MONTH)
               var month=calendar.get(Calendar.MONTH)+1
               var year=calendar.get(Calendar.YEAR)

               val view=LayoutInflater.from(this@Create_User).inflate(R.layout.dialogregister,null)
               val dialog=Dialog(this@Create_User)
               val lottieAnimationView=view.findViewById<LottieAnimationView>(R.id.lottieanimationview1)

               dialog.setContentView(view)
               dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
               dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

               dialog.setCanceledOnTouchOutside(false)
               dialog.create()
               dialog.show()

               firebaseAuth.createUserWithEmailAndPassword(edittextuseraccount.text.toString(),edittextpassword.text.toString()).addOnCompleteListener{

                       if (it.isSuccessful){
                          GlobalScope.launch(Dispatchers.Main) {
                              delay(200)
                              val databaseReference=FirebaseDatabase.getInstance().getReference().child("Workers")

                              var uploadkey:String=databaseReference.push().key.toString()
                              val users=Users(edittextusername.text.toString(),edittextuseraccount.text.toString(),edittextpassword.text.toString(),uploadkey,"$day/$month/$year",firebaseAuth.currentUser!!.uid)
                              databaseReference.child(uploadkey).setValue(users)

                              dialog.dismiss()


                              val databaseReference2=FirebaseDatabase.getInstance().getReference().child(firebaseAuth.currentUser!!.uid)
                              val userMap=UserMap(edittextusername.text.toString(),firebaseAuth.currentUser!!.uid,41.7213480,60.7745550)
                             // 41.5512370,60.6271150 //urgench
                             // 41.6767651,60.7385839 //beruniy
                             // 41.361915,60.613518 yangiariq
                              //41.7213480,60.7745550  //asad tatu
                              databaseReference2.setValue(userMap)
                          }

                       }

                   }



           }
        }
    }

    fun checkedittextempty():Boolean{
        if (edittextusername.text.toString().isEmpty() || edittextuseraccount.text.toString().isEmpty()
            ||edittextpassword.text.toString().isEmpty()

        ){
            edittextusername.setError("Ошибка")
            edittextuseraccount.setError("Ошибка")
            edittextpassword.setError("Ошибка")
            edittextconfirmpassword.setError("Ошибка")
            return false
        }else{
            return  true
        }


    }
}