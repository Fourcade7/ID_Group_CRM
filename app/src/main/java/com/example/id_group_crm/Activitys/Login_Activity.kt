package com.example.id_group_crm.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.id_group_crm.Constants
import com.example.id_group_crm.MainActivity
import com.example.id_group_crm.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.content.SharedPreferences




class Login_Activity : AppCompatActivity() {

   lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor= Color.parseColor("#FFC107")
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        loadText()
        if (Constants.savedtext!=null){
            Constants.useruid= Constants.savedtext!!
            startActivity(Intent(this@Login_Activity,MainActivity::class.java))
        }



        val firebaseAuth= FirebaseAuth.getInstance()
        buttonlogin.setOnClickListener {
            if (edittextuseaccountlogin.text.toString().isEmpty() || edittextuserpasswordlogin.text.toString().isEmpty()){
                Toast.makeText(this@Login_Activity,"Ошибка", Toast.LENGTH_SHORT).show()

            }else{
                lottianimationview2.setAnimation("loadingg.json")
                lottianimationview2.playAnimation()
                lottianimationview2.loop(true)
                buttonlogin.isEnabled=false

//                val view= LayoutInflater.from(this@Login_Activity).inflate(R.layout.dialogregister,null)
//                val dialog= Dialog(this@Login_Activity)
//                val lottieAnimationView=view.findViewById<LottieAnimationView>(R.id.lottieanimationview1)
//
//                dialog.setContentView(view)
//                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//                dialog.setCanceledOnTouchOutside(false)
//                dialog.create()
//                dialog.show()
                firebaseAuth.signInWithEmailAndPassword(edittextuseaccountlogin.text.toString(),edittextuserpasswordlogin.text.toString()).addOnCompleteListener{


                    if (it.isSuccessful){
                        lottianimationview2.setAnimation("checkdone.json")
                        lottianimationview2.playAnimation()
                        lottianimationview2.loop(true)
                        startActivity(Intent(this@Login_Activity,MainActivity::class.java))
                        Constants.useruid=firebaseAuth.currentUser!!.uid

                        saveText(Constants.useruid)
                        finish()
                    }else{
                        lottianimationview2.setAnimation("alert.json")
                        lottianimationview2.playAnimation()
                        lottianimationview2.loop(true)

                        buttonlogin.isEnabled=true
                        Toast.makeText(this@Login_Activity,"Ошибка", Toast.LENGTH_SHORT).show()

                    }


                }
            }


        }
    }



    fun saveText(token:String) {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sharedPreferences.edit()
        ed.putString("SAVED_TEXT", token)
        ed.commit()
       // Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show()
    }
    fun loadText() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        Constants.savedtext= sharedPreferences.getString("SAVED_TEXT", null)


        //Toast.makeText(this, "${Constants.savedtext}", Toast.LENGTH_SHORT).show()
    }

}